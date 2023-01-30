package io.ituknown.httpserver;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;

public class HttpServerVertx extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        super.start();
        Vertx vertx = super.vertx;

        HttpServer httpServer = vertx.createHttpServer();

        httpServer.requestHandler(event -> event.response().setStatusCode(HttpResponseStatus.OK.code()).end("ok"));
        httpServer.listen(8080);
    }
}
