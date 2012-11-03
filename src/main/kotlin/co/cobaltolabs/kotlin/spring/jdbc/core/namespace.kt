package co.cobaltolabs.kotlin.spring.jdbc.core

import java.sql.Connection
import org.springframework.jdbc.core.JdbcOperations
import java.sql.Statement
import java.sql.ResultSet
import org.springframework.dao.EmptyResultDataAccessException
import java.sql.PreparedStatement
import org.springframework.jdbc.support.KeyHolder
import java.sql.CallableStatement
import org.springframework.jdbc.core.SqlParameter


/**
 * Created by IntelliJ IDEA.
 * @author Mario Arias
 * Date: 1/10/12
 * Time: 23:25
 */

public inline fun emptyResultToNull<T>(body: ()->T): T? {
    return try{
        body()
    }catch(e: EmptyResultDataAccessException){
        null
    }
}

public fun JdbcOperations.executeWithConnection<T>(function: (Connection) -> T): T {
    return this.execute(FunctionalConnectionCallback(function))!!
}

public fun JdbcOperations.executeWithStatement<T>(function: (Statement)->T): T {
    return this.execute(FunctionalStatementCallback(function))!!
}

public fun JdbcOperations.query<T>(sql: String, function: (ResultSet)->T): T {
    return this.query(sql, FunctionalResultSetExtractor(function))!!
}

public fun JdbcOperations.query<T>(sql: String, function: (ResultSet, Int)->T): List<T> {
    return this.query(sql, FunctionalRowMapper(function))!!
}

public fun JdbcOperations.queryForObject<T>(sql: String, function: (ResultSet, Int)->T): T {
    return this.queryForObject(sql, FunctionalRowMapper(function))!!
}

public fun JdbcOperations.queryForNullableObject<T>(sql: String, function: (ResultSet, Int)->T): T? {
    return emptyResultToNull{ this.queryForObject(sql, function) }
}

public fun JdbcOperations.executeWithConnectionAndPreparedStatement<T>(statementCreator: (Connection)->PreparedStatement, action: (PreparedStatement)->T): T {
    return this.execute(FunctionalPreparedStatementCreator(statementCreator), FunctionalPreparedStatementCallback(action))!!
}

public fun JdbcOperations.executeWithStringAndPreparedStatement<T>(sql: String, action: (PreparedStatement)->T): T {
    return this.execute(sql, FunctionalPreparedStatementCallback(action))!!
}

public fun JdbcOperations.query<T>(statementCreator: (Connection)->PreparedStatement, action: (ResultSet)->T): T {
    return this.query(FunctionalPreparedStatementCreator(statementCreator), FunctionalResultSetExtractor(action))!!
}

public fun JdbcOperations.query<T>(sql: String, statementSetter: (PreparedStatement)->Unit, action: (ResultSet)->T): T {
    return this.query(sql, FunctionalPreparedStatementSetter(statementSetter), FunctionalResultSetExtractor(action))!!
}

public fun JdbcOperations.query<T>(sql: String, args: Array<out Any>, argTypes: IntArray, function: (ResultSet)->T): T {
    return this.query(sql, args, argTypes, FunctionalResultSetExtractor(function))!!
}

public fun JdbcOperations.query<T>(sql: String, args: Array<out Any>, function: (ResultSet)->T): T {
    return this.query(sql, args, FunctionalResultSetExtractor(function))!!
}

public fun JdbcOperations.query<T>(sql: String, function: (ResultSet)->T, vararg args: Any): T {
    return if(args.isEmpty()){
        this.query(sql, FunctionalResultSetExtractor(function))!!
    }else{
        this.query(sql, args, FunctionalResultSetExtractor(function))!!
    }
}

public fun JdbcOperations.query<T>(statementCreator: (Connection)->PreparedStatement, function: (ResultSet, Int)->T): List<T> {
    return this.query(FunctionalPreparedStatementCreator(statementCreator), FunctionalRowMapper(function))!!
}

public fun JdbcOperations.query<T>(sql: String, statementSetter: (PreparedStatement)->Unit, action: (ResultSet, Int)->T): List<T> {
    return this.query(sql, FunctionalPreparedStatementSetter(statementSetter), FunctionalRowMapper(action))!!
}

public fun JdbcOperations.query<T>(sql: String, args: Array<out Any>, argTypes: IntArray, function: (ResultSet, Int)->T): List<T> {
    return this.query(sql, args, argTypes, FunctionalRowMapper(function))!!
}

public fun JdbcOperations.query<T>(sql: String, args: Array<out Any>, function: (ResultSet, Int)->T): List<T> {
    return this.query(sql, args, FunctionalRowMapper(function))!!
}

public fun JdbcOperations.query<T>(sql: String, function: (ResultSet, Int)->T, vararg args: Any): List<T> {
    return if(args.isEmpty()){
        this.query(sql, FunctionalRowMapper(function))!!
    }else{
        this.query(sql, args, FunctionalRowMapper(function))!!
    }
}

public fun JdbcOperations.queryForObject<T>(sql: String, args: Array<out Any>, argTypes: IntArray, function: (ResultSet, Int)->T): T {
    return this.queryForObject(sql, args, argTypes, FunctionalRowMapper(function))!!
}

public fun JdbcOperations.queryForObject<T>(sql: String, args: Array<out Any>, function: (ResultSet, Int)->T): T {
    return this.queryForObject(sql, args, FunctionalRowMapper(function))!!
}

public fun JdbcOperations.queryForObject<T>(sql: String, function: (ResultSet, Int)->T, vararg args: Any): T {
    return if(args.isEmpty()){
        this.queryForObject(sql, FunctionalRowMapper(function))!!
    }else{
        this.queryForObject(sql, args, FunctionalRowMapper(function))!!
    }
}

public fun JdbcOperations.queryForNullableObject<T>(sql: String, args: Array<out Any>, argTypes: IntArray, function: (ResultSet, Int)->T): T? {
    return emptyResultToNull{ this.queryForObject(sql, args, argTypes, FunctionalRowMapper(function))!! }
}

public fun JdbcOperations.queryForNullableObject<T>(sql: String, args: Array<out Any>, function: (ResultSet, Int)->T): T? {
    return emptyResultToNull{ this.queryForObject(sql, args, FunctionalRowMapper(function))!! }
}

public fun JdbcOperations.queryForNullableObject<T>(sql: String, function: (ResultSet, Int)->T, vararg args: Any): T? {
    return if(args.isEmpty()){
        emptyResultToNull{ this.queryForObject(sql, FunctionalRowMapper(function))!! }
    }else{
        emptyResultToNull{ this.queryForObject(sql, args, FunctionalRowMapper(function))!! }
    }
}

public fun JdbcOperations.update(function: (Connection)->PreparedStatement): Int {
    return this.update(FunctionalPreparedStatementCreator(function))
}

public fun JdbcOperations.update(function: (Connection)->PreparedStatement, keyHolder: KeyHolder): Int {
    return this.update(FunctionalPreparedStatementCreator(function), keyHolder)
}

public fun JdbcOperations.update(sql: String, function: (PreparedStatement)->Unit): Int {
    return this.update(sql, FunctionalPreparedStatementSetter(function))
}

public  fun JdbcOperations.batchUpdate<T>(sql: String, batchArgs: Collection<T>, batchSize: Int, f: (PreparedStatement, T)->Unit): Array<IntArray?> {
    return this.batchUpdate(sql, batchArgs, batchSize, FunctionalParameterizedPreparedStatementSetter(f))!!
}

public fun JdbcOperations.executeWithConnectionAndCallableStatement<T>(statementCreator: (Connection)->CallableStatement, action: (CallableStatement)->T): T {
    return this.execute(FunctionalCallableStatementCreator(statementCreator), FunctionalCallableStatementCallback(action))!!
}

public fun JdbcOperations.executeWithStringAndCallableStatement<T>(sql: String, action: (CallableStatement)->T): T {
    return this.execute(sql, FunctionalCallableStatementCallback(action))!!
}

public fun JdbcOperations.call(f: (Connection)->CallableStatement, parameters: List<SqlParameter>): Map<String?, Any?> {
    return this.call(FunctionalCallableStatementCreator(f), parameters)!!
}