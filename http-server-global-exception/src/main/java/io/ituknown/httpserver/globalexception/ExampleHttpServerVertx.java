package io.ituknown.httpserver.globalexception;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

public class ExampleHttpServerVertx extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        super.start();

        Router router = Router.router(vertx);
        createRouter(router);

        // handler 404
        router.errorHandler(HttpResponseStatus.NOT_FOUND.code(), ctx -> ctx.json(new JsonObject().put("code", 100).put("message", "请求地址不存在")));

        // handler 500
        router.errorHandler(HttpResponseStatus.INTERNAL_SERVER_ERROR.code(), ctx -> ctx.json(new JsonObject().put("code", 200).put("message", "服务器异常: " + ctx.failure().getMessage())));

        // handler other http err code...

        HttpServer httpSrv = vertx.createHttpServer();
        httpSrv.requestHandler(router);
        httpSrv.listen(8080);
    }

    public static void createRouter(Router router) {
        router.get("/index").handler(e -> e.json(new JsonObject().put("code", 0).put("message", "ok")));
        router.get("/exception").handler(e -> simulationBizException());
        // other 404
    }

    public static void simulationBizException() {
        // 500
        throw new RuntimeException("模拟业务异常");
    }
}
