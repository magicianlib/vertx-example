package io.ituknown.mysql.cli;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.SqlConnection;

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

    private MySQLPool pool;

    private static final MySQLClient INSTANCE = new MySQLClient();

    private final AtomicBoolean initialization = new AtomicBoolean(false);
    private final AtomicBoolean release = new AtomicBoolean(false);

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

    public static Future<Void> release() {
        if (INSTANCE.release.compareAndSet(false, true)) {
            System.out.println("MySQLClient release...");
            return INSTANCE.pool.close();
        }
        return null;
    }

    public static void release(Handler<AsyncResult<Void>> handler) {
        if (INSTANCE.release.compareAndSet(false, true)) {
            System.out.println("MySQLClient release...");
            INSTANCE.pool.close(handler);
        }
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

