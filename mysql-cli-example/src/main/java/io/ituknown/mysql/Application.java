package io.ituknown.mysql;

import io.vertx.core.Vertx;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Application {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new MySQLVertx());

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            CountDownLatch countDownLatch = new CountDownLatch(1);
            vertx.close(event -> countDownLatch.countDown());
            try {
                countDownLatch.await(10L, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }));
    }
}
