package co.cobaltolabs.kotlin.spring.jdbc.core.namedparam

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.jdbc.core.namedparam.SqlParameterSource
import java.sql.PreparedStatement
import co.cobaltolabs.kotlin.spring.jdbc.core.*
import java.util.HashMap
import java.sql.ResultSet

/**
 * Created by IntelliJ IDEA.
 * @author Mario Arias
 * Date: 19/10/12
 * Time: 14:33
 */

public fun NamedParameterJdbcOperations.execute<T>(sql: String, source: SqlParameterSource, action: (PreparedStatement)->T): T {
    return this.execute(sql, source, FunctionalPreparedStatementCallback(action))!!
}

public fun NamedParameterJdbcOperations.execute<T>(sql: String, args: Map<String, out Any>, action: (PreparedStatement)->T): T {
    return this.execute(sql, HashMap(args), FunctionalPreparedStatementCallback(action))!!
}

public fun NamedParameterJdbcOperations.query<T>(sql: String, source: SqlParameterSource, extractor: (ResultSet)->T): T {
    return this.query(sql, source, FunctionalResultSetExtractor(extractor))!!
}

public fun NamedParameterJdbcOperations.query<T>(sql: String, args: Map<String, out Any>, extractor: (ResultSet)->T): T {
    return this.query(sql, HashMap(args), FunctionalResultSetExtractor(extractor))!!
}

public fun NamedParameterJdbcOperations.query<T>(sql: String, source: SqlParameterSource, mapper: (ResultSet, Int)->T): List<T> {
    return this.query(sql, source, FunctionalRowMapper(mapper))!!
}

public fun NamedParameterJdbcOperations.query<T>(sql: String, args: Map<String, out Any>, mapper: (ResultSet, Int)->T): List<T> {
    return this.query(sql, HashMap(args), FunctionalRowMapper(mapper))!!
}

public fun NamedParameterJdbcOperations.queryForObject<T>(sql: String, source: SqlParameterSource, mapper: (ResultSet, Int)->T): T {
    return this.queryForObject(sql, source, FunctionalRowMapper(mapper))!!
}

public fun NamedParameterJdbcOperations.queryForObject<T>(sql: String, args: Map<String, out Any>, mapper: (ResultSet, Int)->T): T {
    return this.queryForObject(sql, HashMap(args), FunctionalRowMapper(mapper))!!
}

public fun NamedParameterJdbcOperations.queryForNullableObject<T>(sql: String, source: SqlParameterSource, mapper: (ResultSet, Int)->T): T? {
    return emptyResultToNull{ this.queryForObject(sql, source, FunctionalRowMapper(mapper))!! }
}

public fun NamedParameterJdbcOperations.queryForNullableObject<T>(sql: String, args: Map<String, out Any>, mapper: (ResultSet, Int)->T): T? {
    return emptyResultToNull{ this.queryForObject(sql, HashMap(args), FunctionalRowMapper(mapper))!! }
}