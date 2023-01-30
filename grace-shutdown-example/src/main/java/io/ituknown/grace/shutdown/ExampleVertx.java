package io.ituknown.grace.shutdown;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class ExampleVertx extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        super.start(startPromise);
    }

    @Override
    public void start() throws Exception {
        super.start();
    }

    @Override
    public void stop(Promise<Void> stopPromise) throws Exception {
        super.stop(stopPromise);
        System.out.println("stopPromise");
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.out.println("stop");
    }
}
