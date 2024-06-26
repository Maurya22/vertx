package com.globalpayex;
import com.globalpayex.routes.AppRouter;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import org.slf4j.LoggerFactory;

import org.slf4j.Logger;


public class FirstHttpServer extends AbstractVerticle {

    private final Logger logger = LoggerFactory.getLogger(FirstHttpServer.class);

    @Override
    public void start() throws Exception {

        Future<HttpServer> serverFuture =vertx.createHttpServer()
                .requestHandler(AppRouter.init(vertx,config()))
//                .requestHandler(request -> request.response().end("Hello, World"))
                .listen(config().getInteger("port"));
        serverFuture.onSuccess(httpServer -> logger.info("Server running on port {}", httpServer.actualPort()));
        serverFuture.onFailure(exception -> logger.info("Server is not running{}",exception.getMessage()));

    }

    public static void main(String[] args) {
        Vertx vertx=Vertx.vertx();
        DeploymentOptions options = new DeploymentOptions()
                .setConfig(new JsonObject()
                        .put("port", 8083)
                        .put("connection_string", "mongodb+srv://admin:admin123@cluster0.0wotfuz.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0")
                        .put("db_name","college-db")
                        .put("useObjectId",true)
                );
        vertx.deployVerticle(new FirstHttpServer(),options);
    }
}
