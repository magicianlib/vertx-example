package io.ituknown.jacksonconvert;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

import java.time.LocalDateTime;

public class ExampleHttpServerVertx extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        super.start();
        Vertx vertx = super.vertx;

        Router router = Router.router(vertx);

        //! 因为配置了 Jackson 消息转换器, 所以这里能正常使用 LocalDateTime
        router.get("/user/:id").produces("application/json;charset=UTF-8").handler(ctx -> {
            String id = ctx.pathParam("id");
            ctx.json(new JsonObject().put("id", id).put("name", "bob").put("date", LocalDateTime.now()));
        });

        HttpServer httpServer = vertx.createHttpServer();
        httpServer.requestHandler(router);
        httpServer.listen(8080);
    }
}
