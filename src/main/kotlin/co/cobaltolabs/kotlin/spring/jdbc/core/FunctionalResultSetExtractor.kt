package co.cobaltolabs.kotlin.spring.jdbc.core

import java.sql.ResultSet
import org.springframework.jdbc.core.ResultSetExtractor

/**
 * Created by IntelliJ IDEA.
 * @author Mario Arias
 * Date: 2/10/12
 * Time: 1:21
 */
public class FunctionalResultSetExtractor<T>(val f:(ResultSet)->T):ResultSetExtractor<T> {
    public override fun extractData(rs: ResultSet?): T? {
        return f(rs!!)
    }
}