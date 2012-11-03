package co.cobaltolabs.kotlin.spring.jdbc.core

import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests
import org.springframework.test.context.ContextConfiguration
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.beans.factory.annotation.Autowired
import org.testng.annotations.Test
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet
import org.testng.Assert.*
import co.cobaltolabs.kotlin.spring.jdbc.User
import org.springframework.util.StopWatch
import org.springframework.dao.EmptyResultDataAccessException

/**
 * Created by IntelliJ IDEA.
 * @author Mario Arias
 * Date: 30/10/12
 * Time: 12:53
 */

ContextConfiguration
public class KotlinJdbcSamplesTest: AbstractTransactionalTestNGSpringContextTests() {
    Autowired private var template: JdbcOperations? = null

    Test public fun testWithRowMapper() {
        assertEquals(template!!.query("select * from users", object:RowMapper<User>{
            public override fun mapRow(rs: ResultSet?, rowNum: Int): User? {
                return User(id = rs!!.getInt("id"),
                        firstName = rs.getString("first_name"),
                        lastName = rs.getString("last_name"),
                        age = rs.getInt("age"))
            }
        })!!.size(), 2)
    }

    Test public fun testWithFunctionalRowMapper() {
        assertEquals(template!!.query("select * from users", FunctionalRowMapper{(rs: ResultSet, rowNum: Int)->
            User(id = rs.getInt("id"),
                    firstName = rs.getString("first_name"),
                    lastName = rs.getString("last_name"),
                    age = rs.getInt("age"))
        })!!.size(), 2)
    }

    Test public fun testWithFunctionRowMapper() {
        assertEquals(template!!.query("select * from users") {(rs: ResultSet, rowNum: Int)->
            User(id = rs.getInt("id"),
                    firstName = rs.getString("first_name"),
                    lastName = rs.getString("last_name"),
                    age = rs.getInt("age"))
        }.size(), 2)
    }

    Test public fun testWithEmptyResult() {
        try{
            template!!.queryForObject("select * from users where id = ?", {(rs: ResultSet, rowNum: Int)->
                User(id = rs.getInt("id"),
                        firstName = rs.getString("first_name"),
                        lastName = rs.getString("last_name"),
                        age = rs.getInt("age"))
            }, -1)
            fail()
        }catch(e: EmptyResultDataAccessException){
            //Expected
        }
    }

    Test public fun testWithEmptyResultToNull() {
        assertNull(
            emptyResultToNull {
                template!!.queryForObject("select * from users where id = ?", {(rs: ResultSet, rowNum: Int)->
                    User(id = rs.getInt("id"),
                            firstName = rs.getString("first_name"),
                            lastName = rs.getString("last_name"),
                            age = rs.getInt("age"))
                }, -1)
            })
    }

    Test public fun testWithNullable() {
        assertNull(
            template!!.queryForNullableObject("select * from users where id = ?", {(rs: ResultSet, rowNum: Int)->
                User(id = rs.getInt("id"),
                        firstName = rs.getString("first_name"),
                        lastName = rs.getString("last_name"),
                        age = rs.getInt("age"))
            }, -1)
        )
    }

    fun performanceMeasure(times: Int, task: String, body: ()->Unit) {
        val watch: StopWatch = StopWatch()
        var i: Int = 0
        watch.start("$task :$times")
        while(i < times){
            body()
            i++
        }
        watch.stop()
        println(watch.prettyPrint())
    }

    public fun testPerformance() {
        performanceMeasure(100, "Row Mapper") {
            testWithRowMapper()
        }
        performanceMeasure(100, "Functional Row Mapper") {
            testWithFunctionalRowMapper()
        }
        performanceMeasure(100, "Function") {
            testWithFunctionRowMapper()
        }

        performanceMeasure(1000, "Function") {
            testWithFunctionRowMapper()
        }

        performanceMeasure(1000, "Functional Row Mapper") {
            testWithFunctionalRowMapper()
        }

        performanceMeasure(1000, "Row Mapper") {
            testWithRowMapper()
        }

        performanceMeasure(1000, "Row Mapper") {
            testWithRowMapper()
        }

        performanceMeasure(1000, "Functional Row Mapper") {
            testWithFunctionalRowMapper()
        }

        performanceMeasure(1000, "Function") {
            testWithFunctionRowMapper()
        }


    }
}