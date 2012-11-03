package co.cobaltolabs.kotlin.spring.jdbc.core

import java.sql.Connection
import org.springframework.jdbc.core.ConnectionCallback

/**
 * Created by IntelliJ IDEA.
 * @author Mario Arias
 * Date: 1/10/12
 * Time: 23:19
 */
public class FunctionalConnectionCallback<T>(val f:(Connection) -> T):ConnectionCallback<T> {
    public override fun doInConnection(con: Connection?): T? {
        return f(con!!)
    }
}