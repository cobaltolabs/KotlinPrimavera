package co.cobaltolabs.kotlin.spring.jdbc.core

import co.cobaltolabs.kotlin.spring.jdbc.User
import java.sql.ResultSet
import org.springframework.jdbc.core.RowMapper

/**
 * Created by IntelliJ IDEA.
 * @author Mario Arias
 * Date: 30/10/12
 * Time: 13:44
 */
public class UserMapper: RowMapper<User>{
    public override fun mapRow(rs: ResultSet?, rowNum: Int): User? {
        return User(id = rs!!.getInt("id"),
                firstName = rs.getString("first_name"),
                lastName = rs.getString("last_name"),
                age = rs.getInt("age"))
    }
}