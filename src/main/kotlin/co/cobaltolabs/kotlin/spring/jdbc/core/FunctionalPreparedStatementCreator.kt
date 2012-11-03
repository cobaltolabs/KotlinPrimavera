package co.cobaltolabs.kotlin.spring.jdbc.core

import java.sql.Connection
import java.sql.PreparedStatement
import org.springframework.jdbc.core.PreparedStatementCreator

/**
 * Created by IntelliJ IDEA.
 * @author Mario Arias
 * Date: 2/10/12
 * Time: 10:12
 */
public class FunctionalPreparedStatementCreator(val f: (Connection)->PreparedStatement): PreparedStatementCreator {
    public override fun createPreparedStatement(con: Connection?): PreparedStatement? {
        return f(con!!)
    }
}