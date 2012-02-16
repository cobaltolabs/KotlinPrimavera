package com.cobaltolabs.kotlin.spring.jdbc.core.simple


/**
 * Created by IntelliJ IDEA.
 * @author Mario Arias
 * Date: 15/02/12
 * Time: 20:58
 */

import org.springframework.test.context.ContextConfiguration
import org.testng.annotations.Test
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.testng.Assert.*
import java.util.Map
import java.util.HashMap
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests
import java.sql.ResultSet
import com.cobaltolabs.kotlin.spring.jdbc.TestBean


ContextConfiguration class SimpleJdbcTemplateTest:AbstractTransactionalTestNGSpringContextTests() {

    Autowired var template:SimpleJdbcTemplate? = null

    val mapper = {(rs:ResultSet, i:Int):TestBean ->
        val bean = TestBean()
        bean.id = rs.getInt("id")
        bean.description = rs.getString("description")
        bean.createDate = rs.getDate("creata_date")
        bean
    }

    Test fun testQueryForObject() {
        val bean:TestBean = template.sure().queryForObject("select * from test_bean where id = ?", mapper, "python")

        assertEquals(bean.id, 1)
        assertEquals(bean.description, "python")
    }

    Test fun testQueryForInt() {
        assertEquals(1, template?.queryForInt("select id from test_bean where description = ?", "python"))
    }
}
