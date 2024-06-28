package com.globalpayex;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mail.MailClient;
import io.vertx.ext.mail.MailConfig;
import io.vertx.ext.mail.MailMessage;
import io.vertx.ext.mail.StartTLSOptions;
import io.vertx.ext.mongo.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailVerticle extends AbstractVerticle {
    private MongoClient mongoClient;
    private static final Logger logger = LoggerFactory.getLogger(EmailVerticle.class);
    private MailClient mailClient;

    @Override
    public void start() throws Exception {
        mongoClient = MongoClient.createShared(vertx,config());
        MailConfig mailConfig = new MailConfig()
                .setHostname(config().getString("emailHostname"))
                .setPort(config().getInteger("emailPort"))
                .setStarttls(StartTLSOptions.REQUIRED)
                .setUsername(config().getString("emailUserName"))
                .setPassword(config().getString("emailPassword"));
        mailClient = MailClient.create(vertx,mailConfig);
        vertx
                .eventBus()
                .consumer("new.student",this::handleNewStudent);

    }

    private void handleNewStudent(Message<JsonObject> message) {
        String newStudentId = message.body().getString("_id");
        JsonObject query = new JsonObject()
                .put("_id",newStudentId);
        this.mongoClient.findOne("students",query,null)
                .onSuccess(this::handleStudentJson)
                .onFailure(this::handleStudentFailure);


    }

    private void handleStudentJson(JsonObject studentDbJson) {
        if (studentDbJson != null) {

            MailMessage message = new MailMessage();
            message.setFrom(config().getString("emailUserName"));
            message.setTo(studentDbJson.getString("mail"));
            message.setSubject("Welcome to the College Portal");
            message.setText(
                    String.format("Hey %s, Welcome to the college portal",studentDbJson.getString("username")));


            mailClient.sendMail(message)
                    .onSuccess(msg -> logger.info("email send to student {}", studentDbJson.getString("mail")))
                    .onFailure(exception -> logger.info("error in getting student {}", exception.getMessage()));
        }
    }

    private void handleStudentFailure(Throwable throwable) {
        logger.info("error in getting student {}", throwable.getMessage());
    }
}
