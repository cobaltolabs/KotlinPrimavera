package co.cobaltolabs.kotlin.spring.jdbc.core


import org.springframework.test.context.ContextConfiguration
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcOperations
import org.testng.annotations.Test
import java.sql.Connection
import org.testng.Assert.*
import java.sql.Statement
import java.sql.ResultSet
import co.cobaltolabs.kotlin.spring.TestBean
import org.springframework.dao.EmptyResultDataAccessException
import java.sql.PreparedStatement
import java.sql.Types
import java.sql.Date
import org.springframework.jdbc.support.GeneratedKeyHolder
import java.sql.CallableStatement
import org.springframework.jdbc.core.SqlParameterValue


/**
 * Created by IntelliJ IDEA.
 * @author Mario Arias
 * Date: 1/10/12
 * Time: 23:26
 */
ContextConfiguration
public class KotlinJdbcOperationsTests: JdbcCommons() {
    Autowired var template: JdbcTemplate? = null

    val select1 = "$select where id = 1"
    val selectById = "$select where id = ?"

    val selectIdByDescription = "$selectId where description = ?"
    val selectIdPython = "$selectId where description = 'python'"
    val selectGreatherThan = "$select where id > ?"
    val selectEmpty = "$select where id = -1"

    val insert = "insert into test_bean(description) values(?)"

    val statementCreator: (Connection) -> PreparedStatement = {(con: Connection)->
        val st = con.prepareStatement(selectIdByDescription)
        st!!.setString(1, python)
        st
    }

    val callableStatementCreator: (Connection) -> CallableStatement = {(con: Connection)->
        val st = con.prepareCall(selectIdByDescription)
        st!!.setString(1, python)
        st
    }



    val callableAction = {(st: CallableStatement)->
        val rs = st.executeQuery()
        rs.next()
        rs.getInt("id")
    }

    Test fun testExecuteWithConnection() {
        template!!.executeWithConnection{
            val prepareStatement = it.prepareStatement(selectIdByDescription)
            prepareStatement!!.setString(1, python)
            val resultSet = prepareStatement.executeQuery();
            assertTrue(resultSet.next())
            assertEquals(resultSet.getInt("id"), 1)
            resultSet.close()
        }


    }

    Test fun testExecuteWithStatement() {
        template!!.executeWithStatement{
            val resultSet = it.executeQuery(select1)
            assertTrue(resultSet.next())
            assertEquals(resultSet.getString(description), python)
            resultSet.close()
        }
    }

    Test fun testQuery() {

        assertEquals(template!!.query(select1) {(rs: ResultSet)->
            rs.next()
            rs.getString(description)
        }, python)

        template!!.query(select1) {(rs: ResultSet)->
            rs.next()
            assertEquals(rs.getString(description), python)
        }

        assertEquals(template!!.query(select, mapperFunction).size(), 5)


        assertEquals(template!!.query(statementCreator, rsFunction), 1)

        assertEquals(template!!.query(selectIdByDescription, array<Any>(python), intArray(Types.VARCHAR), rsFunction), 1)

        assertEquals(template!!.query(selectIdByDescription, array<Any>(python), rsFunction), 1)

        assertEquals(template!!.query(selectIdByDescription, rsFunction, python), 1)

        assertEquals(template!!.query({(con: Connection)->
            con.prepareStatement(select)!!
        }, mapperFunction).size(), 5)

        assertEquals(template!!.query(selectGreatherThan,
                {(stmt: PreparedStatement)->
                    stmt.setInt(1, 1)
                }, mapperFunction).size(), 4)

        assertEquals(template!!.query(selectGreatherThan, array<Any>(1), intArray(Types.INTEGER), mapperFunction).size(), 4)

        assertEquals(template!!.query(selectGreatherThan, array<Any>(1), mapperFunction).size(), 4)

        assertEquals(template!!.query(selectGreatherThan, mapperFunction, 1).size(), 4)

        assertEquals(template!!.query(selectIdByDescription,
                {(stmt: PreparedStatement)->
                    stmt.setString(1, python)
                }, rsFunction), 1)

    }

    Test fun testQueryForObject() {
        assertEquals(template!!.queryForObject(select1, mapperFunction).description, python)

        assertEquals(template!!.queryForObject(selectById, array<Any>(1), intArray(Types.INTEGER), mapperFunction).description, python)

        assertEquals(template!!.queryForObject(selectById, array<Any>(1), mapperFunction).description, python)

        assertEquals(template!!.queryForObject(selectById, mapperFunction, 1).description, python)

        validateEmptyResult{
            template!!.queryForObject(selectEmpty, mapperFunction)
        }

        validateEmptyResult{
            template!!.queryForObject(selectById, array<Any>(-1), intArray(Types.INTEGER), mapperFunction)
        }

        validateEmptyResult{
            template!!.queryForObject(selectById, array<Any>(-1), mapperFunction)
        }

        validateEmptyResult{
            template!!.queryForObject(selectById, mapperFunction, -1)
        }
    }



    Test fun testQueryForNullableObject() {
        assertEquals(template!!.queryForNullableObject(select1, mapperFunction)!!.description, python)

        assertEquals(template!!.queryForNullableObject(selectById, array<Any>(1), intArray(Types.INTEGER), mapperFunction)!!.description, python)

        assertEquals(template!!.queryForNullableObject(selectById, array<Any>(1), mapperFunction)!!.description, python)

        assertEquals(template!!.queryForNullableObject(selectById, mapperFunction, 1)!!.description, python)

        assertNull(template!!.queryForNullableObject(selectEmpty, mapperFunction))

        assertNull(template!!.queryForNullableObject(selectById, array<Any>(-1), intArray(Types.INTEGER), mapperFunction))

        assertNull(template!!.queryForNullableObject(selectById, array<Any>(-1), mapperFunction))

        assertNull(template!!.queryForNullableObject(selectById, mapperFunction, -1))
    }

    Test fun testExecuteWithStringAndPreparedStatement() {
        assertEquals(template!!.executeWithStringAndPreparedStatement(selectIdPython, action), 1)
    }

    Test fun testExecuteWithStringAndCallableStatement() {
        assertEquals(template!!.executeWithStringAndCallableStatement(selectIdPython, action), 1)
    }

    Test fun testExecuteWithConnectionAndPreparedStatement() {
        assertEquals(template!!.executeWithConnectionAndPreparedStatement(statementCreator, action), 1)
    }

    Test fun testExecuteWithConnectionAndCallableStatement() {
        assertEquals(template!!.executeWithConnectionAndCallableStatement(callableStatementCreator, callableAction), 1)
    }

    Test fun testUpdate() {
        assertEquals(template!!.update{(con: Connection)->
            val ps = con.prepareStatement("update test_bean set create_date = ?")
            ps!!.setDate(1, Date(System.currentTimeMillis()))
            ps
        }, 5)


        assertEquals(template!!.update({(con: Connection)->
            val ps = con.prepareStatement(insert)
            ps!!.setString(1, "Haxe")
            ps
        }, GeneratedKeyHolder()), 1)

        assertEquals(template!!.update("update test_bean set create_date = ?") {
            (ps: PreparedStatement)->
            ps.setDate(1, Date(System.currentTimeMillis()))

        }, 6)

    }

    Test fun testBatchUpdate() {
        template!!.batchUpdate(insert, arrayList("clojure", "haxe", "objective-c", "erlang"), 4) {
            (ps: PreparedStatement, t: String)->
            ps.setString(1, t)
        }

        assertEquals(count(), 9)
    }

    Test fun testCall() {
        /*val map = template!!.call({(conn: Connection)->
            conn.prepareCall(insert)!!
        }, arrayList(SqlParameterValue(Types.VARCHAR, "Io")))

        assertEquals(count(),6)

        println(map)*/
    }




}