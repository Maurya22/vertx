package com.globalpayex.routes;

import com.globalpayex.entities.Book;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;

public class AppRouter {
    public static Router init(Vertx vertx, JsonObject config){


        Router router = Router.router(vertx);
        router.post().handler(BodyHandler.create());
        router =BooksRoute.init(router);
        router =StudentsRoute.init(router,vertx,config);

        return  router;
    }
}
