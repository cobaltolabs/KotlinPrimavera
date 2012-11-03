package co.cobaltolabs.kotlin.spring.jdbc.core

import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter
import java.sql.PreparedStatement

/**
 * Created by IntelliJ IDEA.
 * @author Mario Arias
 * Date: 13/10/12
 * Time: 12:44
 */
public class FunctionalParameterizedPreparedStatementSetter<T>(val f: (PreparedStatement, T)->Unit): ParameterizedPreparedStatementSetter<T> {
    public override fun setValues(ps: PreparedStatement?, argument: T?) {
        f(ps!!, argument!!)
    }
}