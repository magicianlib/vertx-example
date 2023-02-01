package io.ituknown.httpserver.globalexception;

import io.vertx.core.Vertx;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Application {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new ExampleHttpServerVertx());

        // shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            CountDownLatch countDownLatch = new CountDownLatch(1);
            vertx.close(event -> countDownLatch.countDown());
            try {
                System.out.println("System EXIT!");
                // Max await 10s
                countDownLatch.await(10L, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }));
    }
}