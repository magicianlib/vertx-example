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

        HttpServer httpSrv = vertx.createHttpServer();

        // handler router
        httpSrv.requestHandler(mainRouter);

        // handler connection
        httpSrv.connectionHandler(con -> {
            System.out.println("连接: " + con.remoteAddress(true).hostAddress());
            con.closeHandler(e -> System.out.println("连接 " + con.remoteAddress(true).hostAddress() + " 关闭"));
            con.exceptionHandler(e -> System.out.println("连接 " + con.remoteAddress(true).hostAddress() + " 异常: " + e.getMessage()));
        });

        httpSrv.listen(8080);
    }
}
