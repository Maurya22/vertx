package com.globalpayex;

import com.globalpayex.entities.Book;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import org.slf4j.LoggerFactory;

import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;

public class FirstHttpServer extends AbstractVerticle {

    private final Logger logger = LoggerFactory.getLogger(FiboSeriesVerticle.class);

    @Override
    public void start() throws Exception {
//        dummy database
        List<Book> books = Arrays.asList(
                new Book(1, "java",5600.0,200),
                new Book(2, "Python",3600.0,100),
                new Book(3, "Machine Learning",2600.0,600)
        );
        Router router = Router.router(vertx);
        router.get("/books").handler(routingContext -> {
            JsonArray data = new JsonArray(books);
            routingContext
                    .response()
                    .putHeader("Content-Type", "application/json")
                    .end(data.encode());
        });


        Future<HttpServer> serverFuture =vertx.createHttpServer()
                .requestHandler(router)
//                .requestHandler(request -> request.response().end("Hello, World"))
                .listen(config().getInteger("port"));
        serverFuture.onSuccess(httpServer -> logger.info("Server running on port {}", httpServer.actualPort()));
        serverFuture.onFailure(exception -> logger.info("Server is not running{}",exception.getMessage()));



    }

    public static void main(String[] args) {
        Vertx vertx=Vertx.vertx();
        DeploymentOptions options = new DeploymentOptions()
                .setConfig(new JsonObject().put("port", 8083));
        vertx.deployVerticle(new FirstHttpServer(),options);
    }
}
