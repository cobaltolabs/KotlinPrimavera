package co.cobaltolabs.kotlin.spring.jdbc.core

import java.sql.PreparedStatement
import org.springframework.jdbc.core.PreparedStatementCallback

/**
 * Created by IntelliJ IDEA.
 * @author Mario Arias
 * Date: 2/10/12
 * Time: 10:16
 */
public class FunctionalPreparedStatementCallback<T>(val f: (PreparedStatement)->T): PreparedStatementCallback<T> {
    public override fun doInPreparedStatement(ps: PreparedStatement?): T? {
        return f(ps!!)
    }
}