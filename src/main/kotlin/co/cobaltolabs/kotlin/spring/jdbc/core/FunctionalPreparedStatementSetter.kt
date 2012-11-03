package co.cobaltolabs.kotlin.spring.jdbc.core

import java.sql.PreparedStatement
import org.springframework.jdbc.core.PreparedStatementSetter

/**
 * Created by IntelliJ IDEA.
 * @author Mario Arias
 * Date: 2/10/12
 * Time: 15:17
 */
public class FunctionalPreparedStatementSetter(val f:(PreparedStatement)->Unit):PreparedStatementSetter {
    public override fun setValues(ps: PreparedStatement?) {
        f(ps!!)
    }
}