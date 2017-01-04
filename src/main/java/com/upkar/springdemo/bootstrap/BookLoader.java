package com.upkar.springdemo.bootstrap;

import com.upkar.springdemo.model.Book;
import com.upkar.springdemo.model.BookFactory;
import com.upkar.springdemo.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by upkar on 1/3/17.
 */
@Component
public class BookLoader {

    private Logger logger = LoggerFactory.getLogger(BookLoader.class);

    @Autowired
    private BookRepository bookRepo;

    public BookLoader(BookRepository repo){
        this.bookRepo = repo;
    }

    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent ev){
        logger.info("BookLoader called handleContextRefresh, loading books");
        List<Book> books = loadBooks();
        for(Book b : books){
            bookRepo.save(b);
        }
    }

    public static List<Book> loadBooks(){
        BookFactory bFactory = new BookFactory();
        List<Book> bookList = new ArrayList<>();

        Book b = bFactory.setNewId("978-0641723445")
                .setNewISBN("0786838655")
                .setNewTitle("The Lightning Thief")
                .setnewGenre("fantasy")
                .setNewAuthors(Arrays.asList("Rick Riordan"))
                .setNewCat(Arrays.asList("book","hardcover"))
                .build();
        bookList.add(b);

        b = bFactory.setNewId("978-1423103349")
                .setNewISBN("1423103343")
                .setNewTitle("The Sea of Monsters")
                .setnewGenre("fantasy")
                .setNewAuthors(Arrays.asList("Rick Riordan"))
                .setNewCat(Arrays.asList("book","paperback"))
                .build();
        bookList.add(b);

        b = bFactory.setNewId("978-1857995879")
                .setNewISBN("1857995872")
                .setNewTitle("Sophie's World : The Greek Philosophers")
                .setnewGenre("fantasy")
                .setNewAuthors(Arrays.asList("Jostein Gaarder"))
                .setNewCat(Arrays.asList("book","paperback"))
                .build();
        bookList.add(b);


        b = bFactory.setNewId("978-1933988177")
                .setNewISBN("7115251770")
                .setNewTitle("Lucene in Action, Second Edition")
                .setnewGenre("IT")
                .setNewAuthors(Arrays.asList("Michael McCandless", "Erik Hatcher", "Otis Gospodnetic"))
                .setNewCat(Arrays.asList("book","paperback"))
                .build();
        bookList.add(b);

        b = bFactory.setNewId("978-1935589679")
                .setNewISBN("7125251770")
                .setNewTitle("A Guide to the Project Management Body of Knowledge")
                .setnewGenre("IT")
                .setNewAuthors(Arrays.asList())
                .setNewCat(Arrays.asList("book","paperback"))
                .build();
        bookList.add(b);

        return bookList;
    }
}
