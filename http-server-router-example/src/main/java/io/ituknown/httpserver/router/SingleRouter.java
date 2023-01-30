package io.ituknown.httpserver.router;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

public class SingleRouter {

    public static void createUserRouter(final Vertx vertx, final Router mainRouter) {

        //! GET /version
        mainRouter.get("/version").produces("text/html;charset=UTF-8").handler(ctx -> {
            Buffer version = Buffer.buffer("application version\n\n");
            version.appendString("2023-01-01 init\n");
            ctx.end(version);
        });

        //! GET /user/1
        mainRouter.get("/user/:id").produces("application/json;charset=UTF-8").handler(ctx -> {
            String id = ctx.pathParam("id");
            ctx.json(new JsonObject().put("id", id).put("name", "bob"));
        });

        //! GET /query?q=
        mainRouter.get("/query").handler(ctx -> {
            String q = ctx.request().getParam("q");
            ctx.end("you question is: \"" + q + "\"");
        });

        //! GET /shutdown
        mainRouter.get("/shutdown").handler(ctx -> {
            ctx.end("shutdown!");
            vertx.close();
        });
    }
}