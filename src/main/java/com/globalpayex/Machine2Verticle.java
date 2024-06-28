package com.globalpayex;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Machine2Verticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(Machine2Verticle.class);

    @Override
    public void start() throws Exception {

        DeploymentOptions options = new DeploymentOptions()
                .setConfig(new JsonObject()
                        .put("port", 8083)
                        .put("connection_string", "mongodb+srv://admin:admin123@cluster0.0wotfuz.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0")
                        .put("db_name","college-db")
                        .put("useObjectId",true)
                        .put("emailHostname","stmp.gmail.com")
                        .put("emailPort", 587)
                        .put("emailUserName","patelshrutam000@gmail.com")
                        .put("emailPassword", "xily xday fcjq jilw")
                );

        vertx.deployVerticle(new StatisticalVerticle(), options);
        vertx.deployVerticle(new EmailVerticle(),options);
        logger.info("Machine2 Verticle deployed");
    }

    public static void main(String[] args) {
        Vertx.clusteredVertx(new VertxOptions())
                .onSuccess(vertx -> {
                 vertx.deployVerticle(new Machine2Verticle());
                })
                .onFailure(exception -> logger.info("error deploying machine1 {}",exception.getMessage()));
    }
}
