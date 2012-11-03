package co.cobaltolabs.kotlin.spring.jdbc.core.namedparam

import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests
import org.springframework.test.context.ContextConfiguration
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.testng.annotations.Test
import co.cobaltolabs.kotlin.spring.jdbc.core.JdbcCommons
import co.cobaltolabs.kotlin.spring.TestBean
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.testng.Assert.*

/**
 * Created by IntelliJ IDEA.
 * @author Mario Arias
 * Date: 19/10/12
 * Time: 14:43
 */
ContextConfiguration
public class KotlinNamedParameterJdbcOperationsTest: JdbcCommons() {

    Autowired var template: NamedParameterJdbcTemplate? = null

    val selectIdByDescription = "$selectId where description = :description"

    val selectByIdGreatherThan = "$select where id > :id"

    val selectById = "$select where id = :id"


    Test fun testExecute() {

        assertEquals(template!!.execute(selectIdByDescription, BeanPropertySqlParameterSource(TestBean(description = python)), action), 1)

        assertEquals(template!!.execute(selectIdByDescription, hashMap(description to python), action), 1)
    }

    Test fun testQuery() {
        assertEquals(template!!.query(selectIdByDescription, BeanPropertySqlParameterSource(TestBean(description = python)), rsFunction), 1)

        assertEquals(template!!.query(selectIdByDescription, hashMap(description to python), rsFunction), 1)

        assertEquals(template!!.query(selectByIdGreatherThan, BeanPropertySqlParameterSource(TestBean(id = 1)), mapperFunction).size(), 4)

        assertEquals(template!!.query(selectByIdGreatherThan, hashMap(id to 1), mapperFunction).size(), 4)
    }

    Test fun testQueryForObject() {
        assertEquals(template!!.queryForObject(selectById, BeanPropertySqlParameterSource(TestBean(id = 1)), mapperFunction).description, python)

        validateEmptyResult {
            template!!.queryForObject(selectById, BeanPropertySqlParameterSource(TestBean(id = -1)), mapperFunction)
        }
    }

    Test fun testQueryForNullableObject() {
        assertEquals(template!!.queryForNullableObject(selectById, BeanPropertySqlParameterSource(TestBean(id = 1)), mapperFunction)!!.description, python)

        assertNull(template!!.queryForNullableObject(selectById, BeanPropertySqlParameterSource(TestBean(id = -1)), mapperFunction))

    }
}