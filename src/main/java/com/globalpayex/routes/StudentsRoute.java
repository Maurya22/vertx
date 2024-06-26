package com.globalpayex.routes;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

public class StudentsRoute {
    private static final Logger logger = LoggerFactory.getLogger(StudentsRoute.class);
    private static MongoClient mongoClient;

    public static Router init(Router router, Vertx vertx, JsonObject config){
        mongoClient =MongoClient.createShared(vertx,config);

        router.get("/students").handler(StudentsRoute::getAllstudent);
        router.get("/students/:studentId").handler(StudentsRoute::getstudent);
        router.post("/students").handler(StudentsRoute::createNewStudent);

        return router;
    }

    private static void createNewStudent(RoutingContext routingContext) {
        JsonObject responseJson = routingContext.body().asJsonObject();
        Future<String> future = mongoClient.insert("students",responseJson);
        future.onSuccess(studentresult ->
        routingContext
                .response()
                .putHeader("Content-Type","application/json")
                .end(responseJson.encode()));
        future.onFailure(exception->
                routingContext
                        .response()
                        .setStatusCode(501)
                        .end("Server Error"));

    }

    private static void getstudent(RoutingContext routingContext) {
        String studentId =routingContext.pathParam("studentId");
        JsonObject query = new JsonObject()
                .put("_id", new JsonObject().put("$oid",studentId));
        Future<JsonObject> future = mongoClient.findOne("students", query,null);
        future.onSuccess(studentObject -> {
            if (studentObject == null) {
                routingContext
                        .response()
                        .setStatusCode(401)
                        .end("Student Not found");
            } else {
                JsonObject responseJson = mongoDbToResponseJson(studentObject);
                routingContext
                        .response()
                        .putHeader("Content-Type", "application/json")
                        .end(responseJson.encode());
            }
        });



        future.onFailure(exception -> {
            logger.error("error is fetching student {}",exception.getMessage());
            routingContext
                    .response()
                    .setStatusCode(501)
                    .end("Server error");
    });

    }


    private static void getAllstudent(RoutingContext routingContext) {
        List<String> genderQp = routingContext.queryParam("gender");
        List<String> addressQp = routingContext.queryParam("country");
        JsonObject query = new JsonObject();
        if (genderQp.size() > 0) {
            query.put("gender", genderQp.get(0));
        }
        if(addressQp.size()>0){
            query.put("address.country", addressQp.get(0));
        }
        Future<List<JsonObject>> future = mongoClient
                .find("students", query);
        future.onSuccess(studentJsonObjects -> {
            logger.info("student {}", studentJsonObjects);

            List<JsonObject> responseJson = studentJsonObjects
                    .stream()
                    .map(StudentsRoute::mongoDbToResponseJson)
                    .collect(Collectors.toList());
            JsonArray responseData = new JsonArray(responseJson);
            routingContext
                    .response()
                    .putHeader("Content-Type", "application/json")
                    .end(responseData.encode());
        });

        future.onFailure(exception -> {
            logger.error("error is fetching students {}",exception.getMessage());
            routingContext
                    .response()
                    .setStatusCode(501)
                    .end("Server error");
        });

    }

    private static JsonObject mongoDbToResponseJson(JsonObject dbJson) {
        JsonObject responseJson = new JsonObject();
        responseJson.put("_id", dbJson.getString("_id"));
//                dbJson.getJsonObject("_id")
//                .getString("$oid")); // useObjectId - false
        responseJson.put("username",dbJson.getString("username"));
        responseJson.put("gender",dbJson.getString("gender"));
        responseJson.put("mail",dbJson.getString("mail"));
        return responseJson;

    }

}
