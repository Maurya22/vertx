package com.globalpayex;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class StatisticalVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(StatisticalVerticle.class);

    @Override
    public void start() throws Exception {
        vertx
                .eventBus()
                .consumer("new.student",this::handleNewStudentMessage);
    }

    private void handleNewStudentMessage(Message<JsonObject> message) {
        String newStudentId = message.body().getString("_id");
        logger.info("new Student id {}",newStudentId);
        MongoClient mongoClient = MongoClient.createShared(vertx,config());
        mongoClient.find("students",new JsonObject())
                .onSuccess(this::handleStudentDbJson)
                .onFailure(this::handleStudentDbJsonFailure);

    }

    private void handleStudentDbJson(List<JsonObject> studentsDbJson) {
               var result= studentsDbJson
                        .stream()
                        .collect(
                                Collectors.groupingBy(
                                        studentDbJson -> studentDbJson.getString("gender"),
                                        Collectors.counting()
                                )
                        );
                logger.info("recomputed student stats: {}",result);


    }

    private void handleStudentDbJsonFailure(Throwable throwable) {
        logger.info("error in fetching student {}",throwable.getMessage());
    }
}
