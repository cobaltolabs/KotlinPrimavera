package co.cobaltolabs.kotlin.spring.jdbc.core

import java.sql.ResultSet
import org.springframework.jdbc.core.RowMapper

/**
 * Created by IntelliJ IDEA.
 * @author Mario Arias
 * Date: 1/10/12
 * Time: 23:14
 */
public class FunctionalRowMapper<T>(val f: (ResultSet, Int) -> T): RowMapper<T> {
    public override fun mapRow(rs: ResultSet?, rowNum: Int): T? {
        return f(rs!!, rowNum)
    }
}