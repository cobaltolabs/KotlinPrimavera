package co.cobaltolabs.kotlin.spring.jdbc.core

import java.sql.Statement
import org.springframework.jdbc.core.StatementCallback

/**
 * Created by IntelliJ IDEA.
 * @author Mario Arias
 * Date: 2/10/12
 * Time: 0:55
 */
public class FunctionalStatementCallback<T>(val f: (Statement)->T): StatementCallback<T> {
    public override fun doInStatement(stmt: Statement?): T? {
        return f(stmt!!)
    }
}