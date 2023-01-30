package io.ituknown.grace.shutdown;

import io.vertx.core.Vertx;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Application {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new ExampleVertx());

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            CountDownLatch countDownLatch = new CountDownLatch(1);
            vertx.close(event -> countDownLatch.countDown());
            try {
                // Max await 10s
                countDownLatch.await(10L, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }));

        // Test Shutdown
        try {
            TimeUnit.SECONDS.sleep(3L);
            System.out.println("主动调用 exit");
            System.exit(0);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}