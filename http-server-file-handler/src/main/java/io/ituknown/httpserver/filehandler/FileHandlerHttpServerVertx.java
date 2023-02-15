package io.ituknown.httpserver.filehandler;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.List;

public class FileHandlerHttpServerVertx extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        super.start();

        Router router = Router.router(vertx);

        // 文件上传
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
        builtin(router);
        handlerStream(router);
    }

    public static void builtin(Router router) {
        // Vertx 内置文件上传处理类
        router.post("/upload/builtin")
                .handler(BodyHandler.create(System.getProperty("java.io.tmpdir")).setHandleFileUploads(true))
                .blockingHandler(ctx -> {
                    // see:
                    // io.vertx.ext.web.handler.impl.BodyHandlerImpl.BHandler
                    //
                    // filepath 是一个是上传的文件, 该文件名会被 UUID 重写(不带后缀).
                    // 比如 filepath 是: /var/folders/mq/y1gb7qh53rl32lr089tbdrlm0000gn/T/9eebc7dc-44ba-4e8d-82ea-5101dfe2d7a2
                    // 其中 9eebc7dc-44ba-4e8d-82ea-5101dfe2d7a2 就是对应的文件名
                    //
                    List<FileUpload> files = ctx.fileUploads();
                    for (FileUpload file : files) {

                        // 请求参数key, 如 avatar: <input type="file" name="avatar">
                        String name = file.name();

                        // 真实文件名, 如 example.txt
                        String filename = file.fileName();

                        // 被 BodyHandlerImpl 重新后存储到本地的文件路径
                        String filepath = file.uploadedFileName();
                        System.out.println(filepath);

                        // 拿到存储到本地后的文件即可根据业务需要做处理了...
                    }

                    ctx.end("ok");
                });
    }

    public static void handlerStream(Router router) {

        router.post("/upload/handlerStream").blockingHandler(ctx -> {
            HttpServerRequest request = ctx.request();
            request.setExpectMultipart(true);

            // 使用这种方式现在在这里是拿不到文件的
            // List<FileUpload> files = ctx.fileUploads();

            // 这能处理单文件, 不能处理文件数组(多文件上传)
            request.uploadHandler(file -> {

                // 请求参数key, 如 avatar: <input type="file" name="avatar">
                String name = file.name();

                // 真实文件名, 如 example.txt
                String filename = file.filename();

                // 处理文件流
                file.handler(buf -> System.out.println(buf.toString()));

                // 遇到异常
                file.exceptionHandler(e -> request.response().setChunked(true).end("Upload failed" + e.getMessage()));

                // 处理结束
                file.endHandler(e -> request.response().setChunked(true).end("Successfully uploaded file"));
            });
        });
    }
}
