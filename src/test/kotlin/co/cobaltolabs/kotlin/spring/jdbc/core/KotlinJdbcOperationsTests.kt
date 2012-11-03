package co.cobaltolabs.kotlin.spring.jdbc.core

import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests
import org.springframework.test.context.ContextConfiguration
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcOperations
import org.testng.annotations.Test
import java.sql.Connection
import org.testng.Assert.*
import java.sql.Statement
import java.sql.ResultSet


/**
 * Created by IntelliJ IDEA.
 * @author Mario Arias
 * Date: 1/10/12
 * Time: 23:26
 */
ContextConfiguration
public class KotlinJdbcOperationsTests: AbstractTransactionalTestNGSpringContextTests() {
    Autowired var template: JdbcOperations? = null
    val select = "select * from test_bean "
    val select1 = "${select} where id = 1"
    val selectId = "select id from test_bean where description = ?"
    val python = "python"
    val description = "description"

    Test fun testExecuteWithConnection() {
        template.executeWithConnection({(con: Connection)->
            val prepareStatement = con.prepareStatement(selectId)
            prepareStatement!!.setString(1, python)
            val resultSet = prepareStatement.executeQuery();
            assertTrue(resultSet!!.next())
            assertEquals(resultSet.getInt("id"), 1)
            resultSet.close()
        })
    }

    Test fun testExecuteWithStatement() {
        template.executeWithStatement({(stmt: Statement)->
            val resultSet = stmt.executeQuery(select1)
            assertTrue(resultSet!!.next())
            assertEquals(resultSet.getString(description), python)
            resultSet.close()
        })
    }

    Test fun testQuery() {

        assertEquals(template.query(select1, {(rs: ResultSet)->
            rs.next()
            rs.getString(description)
        }), python)

        template.query(select1, {(rs: ResultSet)->
            rs.next()
            assertEquals(rs.getString(description), python)
        })
    }
}