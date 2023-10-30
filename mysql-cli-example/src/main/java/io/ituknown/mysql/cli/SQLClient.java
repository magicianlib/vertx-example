package io.ituknown.mysql.cli;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.*;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;

public final class SQLClient {

    private static final SQLClient INSTANCE = new SQLClient();
    private static final MySQLConnectOptions DEFAULT_CONNECTION_OPTIONS = new MySQLConnectOptions();
    private static final PoolOptions DEFAULT_POOL_OPTIONS = new PoolOptions();

    static {
        DEFAULT_CONNECTION_OPTIONS.setHost("172.21.11.21");
        DEFAULT_CONNECTION_OPTIONS.setPort(3306);
        DEFAULT_CONNECTION_OPTIONS.setUser("root");
        DEFAULT_CONNECTION_OPTIONS.setPassword("``````");
        DEFAULT_CONNECTION_OPTIONS.setDatabase("vertx");

        DEFAULT_POOL_OPTIONS.setMaxSize(5);
    }

    private final AtomicBoolean initialization = new AtomicBoolean(false);
    private final AtomicBoolean release = new AtomicBoolean(false);
    private MySQLPool pool;

    private SQLClient() {
        if (Objects.nonNull(INSTANCE)) {
            throw new RuntimeException("\"SQLClient\" already created, Cannot be created repeatedly");
        }
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

    public static void release() {
        if (INSTANCE.release.compareAndSet(false, true)) {
            System.out.println("SQLClient release...");
            INSTANCE.pool.close().result();
        }
    }

    public static void release(Handler<AsyncResult<Void>> handler) {
        if (INSTANCE.release.compareAndSet(false, true)) {
            System.out.println("SQLClient release...");
            INSTANCE.pool.close(handler);
        }
    }

    //! Query
    public static Query<RowSet<Row>> query(String sql) {
        return INSTANCE.pool.query(sql);
    }

    public static Query<RowSet<Row>> preparedQuery(String sql) {
        return INSTANCE.pool.preparedQuery(sql);
    }

    public static Query<RowSet<Row>> preparedQuery(String sql, PrepareOptions options) {
        return INSTANCE.pool.preparedQuery(sql, options);
    }

    public static <T> Future<T> withConnection(Function<SqlConnection, Future<T>> function) {
        return INSTANCE.pool.withConnection(function);
    }

    //! withConnection

    public static <T> void withConnection(Function<SqlConnection, Future<T>> function, Handler<AsyncResult<T>> handler) {
        INSTANCE.pool.withConnection(function, handler);
    }

    public static <T> Future<T> withTransaction(Function<SqlConnection, Future<T>> function) {
        return INSTANCE.pool.withTransaction(function);
    }

    //! withTransaction

    public static <T> void withTransaction(Function<SqlConnection, Future<T>> function, Handler<AsyncResult<T>> handler) {
        INSTANCE.pool.withTransaction(function, handler);
    }

    private synchronized void initWithOptions(final Vertx vertx, final MySQLConnectOptions connectOptions, final PoolOptions poolOptions, final Consumer<Throwable> onFailure) {
        if (!initialization.compareAndSet(false, true)) {
            return;
        }

        System.out.println("SQLClient init...");

        pool = MySQLPool.pool(vertx, connectOptions, poolOptions);

        // Test Connect
        pool.preparedQuery("SELECT NOW()").execute().onFailure(onFailure::accept);
    }
}

