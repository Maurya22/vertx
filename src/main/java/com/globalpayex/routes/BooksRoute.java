package com.globalpayex.routes;

import com.globalpayex.entities.Book;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BooksRoute {

    private static final Logger logger = LoggerFactory.getLogger(BooksRoute.class);



    private static ArrayList<Book> books;
    private static int lastUserId =3;


    public static Router init(Router router){
        books = new ArrayList<>();
        books.add(new Book(1, "java",5600.0,200));
        books.add(new Book(2, "Python",3600.0,100));
        books.add(new Book(3, "Machine Learning",2600.0,600));

        router.get("/books").handler(BooksRoute::getAllBooks);
        router.get("/books/:bookId").handler(BooksRoute::getBook);
        router.post("/books").handler(BooksRoute::createNewBook);
        return router;
    }

    private static void createNewBook(RoutingContext routingContext) {
          Book book = routingContext.body().asPojo(Book.class);
          book.setId(++lastUserId);
          books.add(book);
          logger.info("new Book {}", book);
          routingContext.response()
                  .setStatusCode(201)
                  .putHeader("Content-Type", "application/json")
                  .end(JsonObject.mapFrom(book).encode());

    }

    private static void getBook(RoutingContext routingContext) {
        int bookId = Integer.parseInt(routingContext.pathParam("bookId"));
        List<Book> founddBookList = books.stream()
                .filter(book -> book.getId()==bookId)
                .collect(Collectors.toList());
        if(!founddBookList.isEmpty())  {
            Book book = founddBookList.get(0);

            routingContext
                    .response()
                    .putHeader("Content-Type", "application/json")
                    .end(JsonObject.mapFrom(book).encode());
        }
        else {
            JsonObject data= new JsonObject()
                    .put("message",String.format("Book with id %s not found",bookId));
            routingContext
                    .response()
                    .putHeader("Content-Type", "application/json")
                    .setStatusCode(404)
                    .end(data.encode());
        }
    }

    private static void getAllBooks(RoutingContext routingContext) {
        JsonArray data = new JsonArray(books);
            routingContext
                    .response()
                    .putHeader("Content-Type", "application/json")
                    .end(data.encode());
    }
}
