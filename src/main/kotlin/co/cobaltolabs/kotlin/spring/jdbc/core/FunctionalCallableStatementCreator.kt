package co.cobaltolabs.kotlin.spring.jdbc.core

import java.sql.Connection
import java.sql.CallableStatement
import org.springframework.jdbc.core.CallableStatementCreator

/**
 * Created by IntelliJ IDEA.
 * @author Mario Arias
 * Date: 16/10/12
 * Time: 12:45
 */
public class FunctionalCallableStatementCreator(val f:(Connection)->CallableStatement):CallableStatementCreator {
    public override fun createCallableStatement(con: Connection?): CallableStatement? {
        return f(con!!)
    }
}