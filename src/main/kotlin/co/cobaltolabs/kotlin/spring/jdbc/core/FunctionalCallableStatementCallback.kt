package co.cobaltolabs.kotlin.spring.jdbc.core

import java.sql.CallableStatement
import org.springframework.jdbc.core.CallableStatementCallback

/**
 * Created by IntelliJ IDEA.
 * @author Mario Arias
 * Date: 16/10/12
 * Time: 12:58
 */
public class FunctionalCallableStatementCallback<T>(val f:(CallableStatement)->T):CallableStatementCallback<T> {
    public override fun doInCallableStatement(cs: CallableStatement?): T? {
        return f(cs!!)
    }
}