package com.cobaltolabs.kotlin.spring.jdbc.core.simple
/**
 * Created by IntelliJ IDEA.
 * @author Mario Arias
 * Date: 15/02/12
 * Time: 22:24
 */

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate
import java.sql.ResultSet
import org.springframework.jdbc.core.RowMapper
import java.util.List

fun SimpleJdbcTemplate.queryForObject<T>(sql:String, mapper:(ResultSet, Int) -> T, vararg args:Any):T {
    return this.queryForObject(sql, FunctionalRowMapper(mapper), args)
}

/*fun SimpleJdbcTemplate.query<T>(sql:String, mapper:(ResultSet, Int) -> T, vararg args:Any):List<T> {
    return this.query(sql, FunctionalRowMapper(mapper), args)
}*/



class FunctionalRowMapper<T>(val f:(ResultSet, Int) -> T):RowMapper<T>{

    override fun mapRow(rs: ResultSet?, rowNum: Int): T {
        return f(rs.sure(), rowNum);
    }
}
