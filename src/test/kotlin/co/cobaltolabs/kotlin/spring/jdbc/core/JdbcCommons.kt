package co.cobaltolabs.kotlin.spring.jdbc.core

import java.sql.ResultSet
import co.cobaltolabs.kotlin.spring.TestBean
import java.sql.PreparedStatement
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests
import org.testng.Assert.fail
import org.springframework.dao.EmptyResultDataAccessException

/**
 * Created by IntelliJ IDEA.
 * @author Mario Arias
 * Date: 20/10/12
 * Time: 18:59
 */
abstract class JdbcCommons: AbstractTransactionalTestNGSpringContextTests() {
    val select = "select * from test_bean "
    val selectId = "select id from test_bean "
    val python = "python"
    val description = "description"
    val id = "id"

    val mapperFunction = {(rs: ResultSet, i: Int)->
        TestBean(rs.getInt(id),
                rs.getString(description),
                rs.getDate("create_date"))
    }

    val action = {(st: PreparedStatement)->
        val rs = st.executeQuery()
        rs.next()
        rs.getInt(id)
    }

    val rsFunction = {(rs: ResultSet)->
        rs.next()
        rs.getInt("id")
    }

    protected fun count(): Int {
        return countRowsInTable("test_bean")
    }

    protected fun validateEmptyResult(f: ()->Unit) {
        try{
            f()
            fail("Function f don't throw a exception")
        }
        catch(e: EmptyResultDataAccessException){
            //expected
        }
    }
}