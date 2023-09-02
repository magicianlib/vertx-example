package io.ituknown.mysql.template.cli;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.*;
import io.vertx.sqlclient.templates.RowMapper;
import io.vertx.sqlclient.templates.SqlTemplate;
import io.vertx.sqlclient.templates.TupleMapper;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;

public final class MySQLClient {

    private MySQLClient() {
        if (Objects.nonNull(INSTANCE)) {
            throw new RuntimeException("\"MySQLClient\" already created, Cannot be created repeatedly");
        }
    }

    private MySQLPool pool = null;

    private static final MySQLClient INSTANCE = new MySQLClient();

    private final AtomicBoolean initialization = new AtomicBoolean(false);
    private final AtomicBoolean release = new AtomicBoolean(false);

    private static final MySQLConnectOptions DEFAULT_CONNECTION_OPTIONS = new MySQLConnectOptions();
    private static final PoolOptions DEFAULT_POOL_OPTIONS = new PoolOptions();

    static {
        DEFAULT_CONNECTION_OPTIONS.setHost("localhost");
        DEFAULT_CONNECTION_OPTIONS.setPort(3306);
        DEFAULT_CONNECTION_OPTIONS.setUser("root");
        DEFAULT_CONNECTION_OPTIONS.setPassword("123456");
        DEFAULT_CONNECTION_OPTIONS.setDatabase("vertx");
        DEFAULT_CONNECTION_OPTIONS.setCharacterEncoding(StandardCharsets.UTF_8.name());

        DEFAULT_POOL_OPTIONS.setMaxSize(5);
        DEFAULT_POOL_OPTIONS.setShared(true);
    }

    public static void init(final Vertx vertx, final Consumer<Throwable> onFailure) {
        INSTANCE.initWithOptions(vertx, DEFAULT_CONNECTION_OPTIONS, DEFAULT_POOL_OPTIONS, onFailure);
    }

    public static void init(final Vertx vertx, final MySQLConnectOptions defaultConnectionOptions, final Consumer<Throwable> onFailure) {
        INSTANCE.initWithOptions(vertx, defaultConnectionOptions, DEFAULT_POOL_OPTIONS, onFailure);
    }

    public static void init(final Vertx vertx, final MySQLConnectOptions connectOptions, final PoolOptions poolOptions, final Consumer<Throwable> onFailure) {
        INSTANCE.initWithOptions(vertx, connectOptions, poolOptions, onFailure);
    }

    private synchronized void initWithOptions(final Vertx vertx, final MySQLConnectOptions connectOptions, final PoolOptions poolOptions, final Consumer<Throwable> onFailure) {
        if (!initialization.compareAndSet(false, true)) {
            return;
        }

        System.out.println("MySQLClient init...");

        pool = MySQLPool.pool(vertx, connectOptions, poolOptions);

        // Test Connect
        pool.preparedQuery("SELECT NOW()").execute().onFailure(onFailure::accept);
    }

    public static void release() {
        if (INSTANCE.release.compareAndSet(false, true)) {
            System.out.println("MySQLClient release...");
            INSTANCE.pool.close();
        }
    }

    public static void release(Handler<AsyncResult<Void>> handler) {
        if (INSTANCE.release.compareAndSet(false, true)) {
            System.out.println("MySQLClient release...");
            INSTANCE.pool.close(handler);
        }
    }

    //! forQuery
    public static SqlTemplate<Map<String, Object>, RowSet<Row>> forTemplateQuery(String sqlTemplate) {
        return SqlTemplate.forQuery(INSTANCE.pool, sqlTemplate);
    }

    public static <R> SqlTemplate<Map<String, Object>, RowSet<R>> forTemplateQuery(String sqlTemplate, Class<R> resultType) {
        return SqlTemplate.forQuery(INSTANCE.pool, sqlTemplate).mapTo(resultType);
    }

    public static <R> SqlTemplate<Map<String, Object>, RowSet<R>> forTemplateQuery(String sqlTemplate, RowMapper<R> rowMapper) {
        return SqlTemplate.forQuery(INSTANCE.pool, sqlTemplate).mapTo(rowMapper);
    }

    /**
     * 查询
     *
     * @param sqlTemplate sql 语句或模板
     * @param resultType  返回映射类型
     * @param paramType   请求参数类型
     * @see examples.TemplateExamples
     */
    public static <T, R> SqlTemplate<T, RowSet<R>> forTemplateQuery(String sqlTemplate, Class<R> resultType, Class<T> paramType) {
        return SqlTemplate.forQuery(INSTANCE.pool, sqlTemplate).mapTo(resultType).mapFrom(paramType);
    }

    public static <T, R> SqlTemplate<T, RowSet<R>> forTemplateQuery(String sqlTemplate, Class<R> resultType, TupleMapper<T> paramType) {
        return SqlTemplate.forQuery(INSTANCE.pool, sqlTemplate).mapTo(resultType).mapFrom(paramType);
    }

    public static <T, R> SqlTemplate<T, RowSet<R>> forTemplateQuery(String sqlTemplate, RowMapper<R> rowMapper, Class<T> paramType) {
        return SqlTemplate.forQuery(INSTANCE.pool, sqlTemplate).mapTo(rowMapper).mapFrom(paramType);
    }

    public static <T, R> SqlTemplate<T, RowSet<R>> forTemplateQuery(String sqlTemplate, RowMapper<R> rowMapper, TupleMapper<T> paramType) {
        return SqlTemplate.forQuery(INSTANCE.pool, sqlTemplate).mapTo(rowMapper).mapFrom(paramType);
    }

    //! forUpdate
    public static SqlTemplate<Map<String, Object>, SqlResult<Void>> forTemplateUpdate(String sqlTemplate) {
        return SqlTemplate.forUpdate(INSTANCE.pool, sqlTemplate);
    }

    public static <T> SqlTemplate<T, SqlResult<Void>> forTemplateUpdate(String sqlTemplate, Class<T> paramType) {
        return SqlTemplate.forUpdate(INSTANCE.pool, sqlTemplate).mapFrom(paramType);
    }

    public static <T> SqlTemplate<T, SqlResult<Void>> forTemplateUpdate(String sqlTemplate, TupleMapper<T> paramType) {
        return SqlTemplate.forUpdate(INSTANCE.pool, sqlTemplate).mapFrom(paramType);
    }

    //! withConnection

    public static <T> Future<T> withConnection(Function<SqlConnection, Future<T>> function) {
        return INSTANCE.pool.withConnection(function);
    }

    public static <T> void withConnection(Function<SqlConnection, Future<T>> function, Handler<AsyncResult<T>> handler) {
        INSTANCE.pool.withConnection(function, handler);
    }

    //! withTransaction

    public static <T> Future<T> withTransaction(Function<SqlConnection, Future<T>> function) {
        return INSTANCE.pool.withTransaction(function);
    }

    public static <T> void withTransaction(Function<SqlConnection, Future<T>> function, Handler<AsyncResult<T>> handler) {
        INSTANCE.pool.withTransaction(function, handler);
    }
}

