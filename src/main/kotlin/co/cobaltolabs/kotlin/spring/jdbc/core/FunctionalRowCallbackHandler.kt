package co.cobaltolabs.kotlin.spring.jdbc.core

import java.sql.ResultSet
import org.springframework.jdbc.core.RowCallbackHandler

/**
 * Created by IntelliJ IDEA.
 * @author Mario Arias
 * Date: 2/10/12
 * Time: 1:41
 */
Deprecated
public class FunctionalRowCallbackHandler(val f: (ResultSet)->Unit): RowCallbackHandler {
    public override fun processRow(rs: ResultSet?) {
        f(rs!!)
    }
}