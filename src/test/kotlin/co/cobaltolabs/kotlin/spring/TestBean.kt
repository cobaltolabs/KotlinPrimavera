package co.cobaltolabs.kotlin.spring

import java.util.Date

/**
 * Created by IntelliJ IDEA.
 * @author Mario Arias
 * Date: 2/10/12
 * Time: 1:31
 */
public class TestBean(var id: Int? = null,
                      var description: String? = null,
                      var createDate: Date? = null){

    public fun toString(): String {
        return description!!
    }
}