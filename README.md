
## Documentation ver 0.1 ##

### Changes on 0.1 ###

* Initial version
* Support classes and extension functions for [`org.springframework.jdbc.core.JdbcOperations`](http://static.springsource.org/spring/docs/3.1.x/javadoc-api/org/springframework/jdbc/core/JdbcOperations.html) and [`org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations`](http://static.springsource.org/spring/docs/3.1.x/javadoc-api/org/springframework/jdbc/core/namedparam/NamedParameterJdbcOperations.html)

### General

#### Maven Repositories

The jars will be uploaded to Maven Repositories in the next few weeks… if you want to use right now, just download the [jar](http://cobaltolabs.com/downloads/KotlinPrimavera-0.1_M3.1.jar) or clone the repo and build the project

##### Package names

The package names for KotlinPrimavera start with `co.cobaltolabs.kotlin.spring` this package name is provisional and will be changed in the next iterations

### Data Access with KotlinPrimavera

KotlinPrimavera offer Support classes and extension functions for [`org.springframework.jdbc.core.JdbcOperations`](http://static.springsource.org/spring/docs/3.1.x/javadoc-api/org/springframework/jdbc/core/JdbcOperations.html) and [`org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations`](http://static.springsource.org/spring/docs/3.1.x/javadoc-api/org/springframework/jdbc/core/namedparam/NamedParameterJdbcOperations.html)

#### Using JdbcOperations with Kotlin

You could use `JdbcOperations` ([`org.springframework.jdbc.core.JdbcTemplate`](http://static.springsource.org/spring/docs/3.1.x/javadoc-api/org/springframework/jdbc/core/JdbcTemplate.html) is the defualt implementation for `JdbcOperations`) as always. Ex:

```kotlin
Test public fun testWithRowMapper() {
        assertEquals(template!!.query("select * from users", object:RowMapper<User>{
            public override fun mapRow(rs: ResultSet?, rowNum: Int): User? {
                return User(id = rs!!.getInt("id"),
                        firstName = rs.getString("first_name"),
                        lastName = rs.getString("last_name"),
                        age = rs.getInt("age"))
            }
        })!!.size(), 2)
    }
```

In the previous example, we're using an anonymous implementation for `RowMapper<T>`

#### Going functional

For Every Interface that have only one method, KotlinPrimavera offers a functional implementation that you could use instead of anonymous implementations. Ex:

```kotlin
import co.cobaltolabs.kotlin.spring.jdbc.core.*

Test public fun testWithFunctionalRowMapper() {
        assertEquals(template!!.query("select * from users", FunctionalRowMapper{(rs: ResultSet, rowNum: Int)->
            User(id = rs.getInt("id"),
                    firstName = rs.getString("first_name"),
                    lastName = rs.getString("last_name"),
                    age = rs.getInt("age"))
        })!!.size(), 2)
    }
```

In the previous example we pass a function `(ResultSet,Int)->T` as parameter in the `FunctionalRowMapper<T>` constructor

This is the complete list of functional implementations


Interface                                 | Functional implementation                           | Function
----------------------------------------- | --------------------------------------------------- | ---------------------------------
[`CallableStatementCallback<T>`](http://static.springsource.org/spring/docs/3.1.x/javadoc-api/org/springframework/jdbc/core/CallableStatementCallback.html)            | `FunctionalCallableStatementCallback<T>`            | `(CallableStatement)->T`
[`CallableStatementCreator`](http://static.springsource.org/spring/docs/3.1.x/javadoc-api/org/springframework/jdbc/core/CallableStatementCreator.html)                | `FunctionalCallableStatementCreator`                | `(Connection)->CallableStatement`
[`ConnectionCallback<T>`](http://static.springsource.org/spring/docs/3.1.x/javadoc-api/org/springframework/jdbc/core/ConnectionCallback.html)                   | `FunctionalConnectionCallback<T>`                   | `(Connection) -> T`
[`ParameterizedPreparedStatementSetter<T>`](http://static.springsource.org/spring/docs/3.1.x/javadoc-api/org/springframework/jdbc/core/ParameterizedPreparedStatementSetter.html) | `FunctionalParameterizedPreparedStatementSetter<T>` | `(PreparedStatement, T)->Unit`
[`PreparedStatementCallback<T>`](http://static.springsource.org/spring/docs/3.1.x/javadoc-api/org/springframework/jdbc/core/PreparedStatementCallback.html)            | `FunctionalPreparedStatementCallback<T>`            | `(PreparedStatement)->T`
[`PreparedStatementCreator`](http://static.springsource.org/spring/docs/3.1.x/javadoc-api/org/springframework/jdbc/core/PreparedStatementCreator.html)                | `FunctionalPreparedStatementCreator`                | `(Connection)->PreparedStatement`
[`PreparedStatementSetter`](http://static.springsource.org/spring/docs/3.1.x/javadoc-api/org/springframework/jdbc/core/PreparedStatementSetter.html)                 | `FunctionalPreparedStatementSetter`                 | `(PreparedStatement)->Unit`
[`ResultSetExtractor<T>`](http://static.springsource.org/spring/docs/3.1.x/javadoc-api/org/springframework/jdbc/core/ResultSetExtractor.html)                   | `FunctionalResultSetExtractor<T>`                   | `(ResultSet)->T`
[`RowCallbackHandler`](http://static.springsource.org/spring/docs/3.1.x/javadoc-api/org/springframework/jdbc/core/RowCallbackHandler.html)                      | `FunctionalResultSetExtractor<Unit>`                | `(ResultSet)->Unit`
[`RowMapper<T>`](http://static.springsource.org/spring/docs/3.1.x/javadoc-api/org/springframework/jdbc/core/RowMapper.html)                            | `FunctionalRowMapper<T>`                            | `(ResultSet, Int) -> T`
[`StatementCallback<T>`](http://static.springsource.org/spring/docs/3.1.x/javadoc-api/org/springframework/jdbc/core/StatementCallback.html)                    | `FunctionalStatementCallback<T>`                    | `(Statement)->T`

As you can see `RowCallbackHandler` don't have his own functional implementation but use `FunctionalResultSetExtractor<Unit>` a nice effect for use high order functions (In fact a `FunctionalRowCallbackHandler` exists but is deprecated… yes, we have deprecated classes on our first release)

#### Using extension functions

Functional implementations are a nice round up, but aren't too functional, KotlinPrimavera offers extension functions that accepts function literals as parameters (This functions are located in the `co.cobaltolabs.kotlin.spring.jdbc.core` namespace). Ex:

```kotlin
import co.cobaltolabs.kotlin.spring.jdbc.core.*

Test public fun testWithFunctionRowMapper() {
        assertEquals(template!!.query("select * from users") {(rs: ResultSet, rowNum: Int)->
            User(id = rs.getInt("id"),
                    firstName = rs.getString("first_name"),
                    lastName = rs.getString("last_name"),
                    age = rs.getInt("age"))
        }.size(), 2)
    }
```

This is the complete list of extension functions


<table>
    <thead>
    <tr>
        <th>JdbcOperations Extension Functions</th>
        <th>Use instead of</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td>
```
executeWithConnection<T>(f: (Connection) -> T): T
```
</td>
        <td>
```
execute<T>(cc: ConnectionCallback<T>?): T?
```
</td>
    </tr>
<tr>
        <td>
```
executeWithStatement<T>(f: (Statement)->T): T
```
</td>
        <td>
```
execute<T>(sc: StatementCallback<T>?): T?
```
</td>
</tr>
<tr>
        <td>
```
query<T>(sql: String,
    f: (ResultSet)->T): T
```
</td>
        <td>
```
query<T>(sql: String?,
    rse: ResultSetExtractor<T>?): T?
```
</td>
</tr>
<tr>
        <td>
```
query<Unit>(sql: String,
    f: (ResultSet)->Unit)
```
</td>
        <td>
```
query(sql: String?,
    rch: RowCallbackHandler)
```
</td>
</tr>
<tr>
        <td>
```
query<T>(sql: String,
    f: (ResultSet, Int)->T): List<T>
```
</td>
        <td>
```
query<T>(sql: String?,
    rm: RowMapper<T>?): MutableList<T>?
```
</td>
</tr>
<tr>
        <td>
```
queryForObject<T>(sql: String,
    f: (ResultSet, Int)->T): T
```
</td>
        <td>
```
queryForObject<T>(sql: String?,
    rm: RowMapper<T>?): T?
```
</td>
</tr>
<tr>
        <td>
```
executeWithConnectionAndPreparedStatement<T>(
  sc: (Connection)->PreparedStatement,
  f: (PreparedStatement)->T): T
```
</td>
        <td>
```
execute<T>(
    psc: PreparedStatementCreator?,
    a: PreparedStatementCallback<T>?): T?
```
</td>
</tr>
<tr>
        <td>
```
executeWithStringAndPreparedStatement<T>(
    sql: String,
    f: (PreparedStatement)->T): T
```
</td>
        <td>
```
execute<T>(
    sql: String,
    a: PreparedStatementCallback<T>?): T?
```
</td>
</tr>
<tr>
        <td>
```
query<T>(sc: (Connection)->PreparedStatement,
    f: (ResultSet)->T): T
```
</td>
        <td>
```
query<T>(
    psc: PreparedStatementCreator?,
    rse: ResultExtractor<T>?): T?
```
</td>
</tr>
<tr>
        <td>
```
query<T>(sql: String,
    pss: (PreparedStatement)->Unit,
    f: (ResultSet)->T): T
```
</td>
        <td>
```
query<T>(sql: String?,
    pss: PreparedStatementSetter?,
    rse: ResultSetExtractor<T>?): T?
```
</td>
</tr>
<tr>
        <td>
```
query<T>(sql: String,
    args: Array<out Any>,
    argTypes: IntArray,
    f: (ResultSet)->T): T
```
</td>
        <td>
```
query<T>(sql: String?,
    args: Array<out Any>?,
    argTypes: IntArray?,
    rse: ResultSetExtractor<T>?): T?

```
</td>
</tr>
<tr>
        <td>
```
query<T>(sql: String,
    args: Array<out Any>,
    f: (ResultSet)->T): T
```
</td>
        <td>
```
query<T>(sql: String?,
    args: Array<out Any>?,
    rse: ResultSetExtractor<T>?): T?
```
</td>
</tr>
<tr>
        <td>
```
query<T>(sql: String,
    f: (ResultSet)->T,
    vararg args: Any): T
```
</td>
        <td>
```
query<T>(sql: String?,
    rse: ResultSetExtractor<T>?,
    vararg args: Any?): T?
```
</td>
</tr>
<tr>
        <td>
```
query<T>(sc: (Connection)->PreparedStatement,
    f: (ResultSet, Int)->T): List<T>
```
</td>
        <td>
```
query<T>(sc: PreparedStatementCreator?,
    rm: RowMapper<T>?): MutableList<T>?
```
</td>
</tr>
<tr>
        <td>
```
query<T>(sql: String,
    ss: (PreparedStatement)->Unit,
    f: (ResultSet, Int)->T): List<T>
```
</td>
        <td>
```
query<T>(sql: String?,
    pss: PreparedStatementSetter?,
    rm: RowMapper<T>?): MutableList<T>?
```
</td>
</tr>
<tr>
        <td>
```
query<T>(sql: String,
    args: Array<out Any>,
    argTypes: IntArray,
    f: (ResultSet, Int)->T): List<T>
```
</td>
        <td>
```
query<T>(sql: String?,
    args: Array<out Any>?,
    argTypes: IntArray?,
    rm: RowMapper<T>?): MutableList<T>?
```
</td>
</tr>
<tr>
        <td>
```
query<T>(sql: String,
    args: Array<out Any>,
    f: (ResultSet, Int)->T): List<T>
```
</td>
        <td>
```
query<T>(sql: String?,
    args: Array<out Any>?,
    rm: RowMapper<T>?): MutableList<T>?
```
</td>
</tr>
<tr>
        <td>
```
query<T>(sql: String,
    f: (ResultSet, Int)->T,
    vararg args: Any): List<T>
```
</td>
        <td>
```
query<T>(sql: String?,
    rm: RowMapper<T>?,
    vararg args: Any?): MutableList<T>?
```
</td>
</tr>
<tr>
        <td>
```
queryForObject<T>(sql: String,
    args: Array<out Any>,
    argTypes: IntArray,
    f: (ResultSet, Int)->T): T
```
</td>
        <td>
```
queryForObject<T>(sql: String?,
    args: Array<out Any>?,
    argTypes: IntArray?,
    rm: RowMapper<T>?): T?
```
</td>
</tr>
<tr>
        <td>
```
queryForObject<T>(sql: String,
    args: Array<out Any>,
    f: (ResultSet, Int)->T): T
```
</td>
        <td>
```
queryForObject<T>(sql: String?,
    args: Array<out Any>?,
    rm: RowMapper<T>?):T?
```
</td>
</tr>
<tr>
        <td>
```
queryForObject<T>(sql: String,
    f: (ResultSet, Int)->T,
    vararg args: Any): T
```
</td>
        <td>
```
queryForObject<T>(sql: String?,
    rm: RowMapper<T>?,
    vararg args: Any?): T?
```
</td>
</tr>
<tr>
        <td>
```
update(f: (Connection)->PreparedStatement): Int
```
</td>
        <td>
```
update(psc: PreparedStatementCreator?): Int
```
</td>
</tr>
<tr>
        <td>
```
update(f: (Connection)->PreparedStatement,
    keyHolder: KeyHolder): Int
```
</td>
        <td>
```
update(psc: PreparedStatementCreator?
    keyHolder: KeyHolder?): Int
```
</td>
</tr>
<tr>
        <td>
```
update(sql: String,
    f: (PreparedStatement)->Unit): Int
```
</td>
        <td>
```
update(sql: String?,
    pss: PreparedStatementSetter?): Int
```
</td>
</tr>
<tr>
        <td>
```
batchUpdate<T>(sql: String,
    batchArgs: Collection<T>,
    batchSize: Int,
    f: (PreparedStatement, T)->Unit):
    Array<IntArray?>
```
</td>
        <td>
```
batchUpdate<T>(sql: String?,
    batchArgs: Collection<T>?,
    batchSize: Int,
    ppss: ParameterizedPreparedStatementSetter?):
    Array<IntArray?>?
```
</td>
</tr>
<tr>
        <td>
```
executeWithConnectionAndCallableStatement<T>(
    csc: (Connection)->CallableStatement,
    f: (CallableStatement)->T): T
```
</td>
        <td>
```
execute<T>(
    csc: CallableStatementCreator?,
    f: CallableStatementCallback<T?> ): T?
```
</td>
</tr>
<tr>
        <td>
```
executeWithStringAndCallableStatement<T>(
    sql: String,
    f: (CallableStatement)->T): T
```
</td>
        <td>
```
execute<T>(
    sql: String?
    f: CallableStatementCallback<T?> ): T?
```
</td>
</tr>

<tr>
        <td>
```
call(
    f: (Connection)->CallableStatement,
    parameters: List<SqlParameter>):
    Map<String?, Any?>
```
</td>
        <td>
```
call(
    csc: CallableStatementCreator?,
    parameters: List<SqlParameter?>?):
    MutableMap<String?, Any?>?
```
</td>
</tr>

    </tbody>
</table>



#### Working with EmptyResultDataAccessException and nulls

For methods that return one single object, JdbcOperations (and NamedParameterJdbcOperations) never return null but will throw a `org.springframework.dao.EmptyResultDataAccessException` if the results are empty. Ex:

```kotlin
Test public fun testWithEmptyResult() {
    try{
        template!!.queryForObject("select * from users where id = ?", {(rs: ResultSet, rowNum: Int)->
            User(id = rs.getInt("id"),
                    firstName = rs.getString("first_name"),
                    lastName = rs.getString("last_name"),
                    age = rs.getInt("age"))
        }, -1)
        fail()//Should never reach this line
    }catch(e: EmptyResultDataAccessException){
        //Expected
    }
}
```

If you want to manage this empty result with a null you can use the function `emptyResultToNull<T>(body: ()->T): T?`. If the code block that you write inside the function throws a `EmptyResultDataAccessException` a `null` will be returned. Ex:

```kotlin
Test public fun testWithEmptyResultToNull() {
    assertNull(
        emptyResultToNull {
            template!!.queryForObject("select * from users where id = ?", {(rs: ResultSet, rowNum: Int)->
                User(id = rs.getInt("id"),
                        firstName = rs.getString("first_name"),
                        lastName = rs.getString("last_name"),
                        age = rs.getInt("age"))
            }, -1)
        })
}
```

There's also a `queryForNullableObject` Extension Function that have the same behaviour, returning null if the result is empty

```kotlin
Test public fun testWithNullable() {
        assertNull(
            template!!.queryForNullableObject("select * from users where id = ?", {(rs: ResultSet, rowNum: Int)->
                User(id = rs.getInt("id"),
                        firstName = rs.getString("first_name"),
                        lastName = rs.getString("last_name"),
                        age = rs.getInt("age"))
            }, -1)
        )
    }
```

#### Using NamedParameterJdbcOperations

KotlinPrimavera also have Extension Functions for `NamedParameterJdbcOperations`

<table>
    <thead>
    <tr>
        <th>NamedParameterJdbcOperations Extension Functions</th>
        <th>Use instead of</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td>
```
execute<T>(sql: String,
    source: SqlParameterSource,
    f: (PreparedStatement)->T): T
```
        </td>
        <td>
```
execute<T>(sql: String?,
    source: SqlParameterSource?,
    psc: PreparedStatementCallback<T>?): T?
```
        </td>
    </tr>
    <tr>
        <td>
```
execute<T>(sql: String,
    args: Map<String, out Any>,
    f: (PreparedStatement)->T): T
```
        </td>
        <td>
```
execute<T>(sql: String?,
    args: Map<String?, out Any?>?,
    psc: PreparedStatementCallback<T>?): T?
```
        </td>
    </tr>
    <tr>
        <td>
```
query<T>(sql: String,
    source: SqlParameterSource,
    f: (ResultSet)->T): T
```
        </td>
        <td>
```
query<T>(sql: String?,
    source: SqlParameterSource?,
    rse: ResultSeetExtractor?): T?
```
        </td>
    </tr>
    <tr>
        <td>
```
query<T>(sql: String,
    args: Map<String, out Any>,
    f: (ResultSet)->T): T
```
        </td>
        <td>
```
query<T>(sql: String?,
    args: Map<String?, out Any?>?,
    rse: ResultSeetExtractor<T>?): T?
```
        </td>
    </tr>
    <tr>
        <td>
```
query<T>(sql: String,
    source: SqlParameterSource,
    f: (ResultSet, Int)->T): List<T>
```
        </td>
        <td>
```
query<T>(sql: String?,
    source: SqlParameterSource?,
    rm: RowMapper<T>?): MutableList<T>?
```
        </td>
    </tr>
    <tr>
        <td>
```
query<T>(sql: String,
    args: Map<String, out Any>,
    f: (ResultSet, Int)->T): List<T>
```
        </td>
        <td>
```
query<T>(sql: String?,
    args: Map<String?, out Any?>?,
    rm: RowMapper<T>?): List<T>?
```
        </td>
    </tr>
    <tr>
        <td>
```
queryForObject<T>(sql: String,
    source: SqlParameterSource,
    f: (ResultSet, Int)->T): T
```
        </td>
        <td>
```
queryForObject<T>(sql: String?,
    source: SqlParameterSource?,
    rm: RowMapper<T>?): T?
```
        </td>
    </tr>
    <tr>
        <td>
```
queryForObject<T>(sql: String,
    args: Map<String, out Any>,
    f: (ResultSet, Int)->T): T
```
        </td>
        <td>
```
queryForObject<T>(sql: String?,
    args: Map<String?, out Any?>?,
    rm: RowMapper<T>?): T?
```
        </td>
    </tr>

    </tobdy>
</table>

