package io.ituknown.httpserver.router;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

public class HttpServerVertx extends AbstractVerticle {

    @Override
    public void start() throws Exception {

        super.start();
        Vertx vertx = super.vertx;


        Router mainRouter = Router.router(vertx);

        // handler main router
        SingleRouter.createUserRouter(vertx, mainRouter);

        // handler multilevel routing
        GroupRouter.createProductRouter(vertx, mainRouter);

        HttpServer httpServer = vertx.createHttpServer();
        httpServer.requestHandler(mainRouter);

        httpServer.listen(8080);
    }
}
