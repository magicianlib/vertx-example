package io.ituknown.httpserver.router;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RequestBody;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class GroupRouter {

    public static void createProductRouter(final Vertx vertx, final Router mainRouter) {
        Router product = Router.router(vertx);

        //! GET /product/:id
        product.get("/:id").handler(ctx -> {
            String id = ctx.pathParam("id");
            ctx.end("get product: " + id);
        });

        //! GET /query?name=
        product.get("/query").handler(ctx -> {
            String name = ctx.get("name");
            ctx.end("get product: " + name);
        });

        //! POST /product/:id
        product.post("/:id").handler(ctx -> {
            String id = ctx.pathParam("id");
            ctx.end("post product: " + id);
        });

        //! POST /product/jsonBody
        product.post("/jsonBody").order(-1).handler(BodyHandler.create()).handler(ctx -> {
            RequestBody body = ctx.body();
            JsonObject entries = body.asJsonObject();
            System.out.println(entries.toString());
            ctx.end("ok");
        });

        mainRouter.route("/product/*").subRouter(product);
    }
}