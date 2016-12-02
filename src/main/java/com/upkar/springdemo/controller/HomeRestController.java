package com.upkar.springdemo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.upkar.springdemo.model.Book;
import com.upkar.springdemo.service.BookService;


@RestController
@RequestMapping("/api")
public class HomeRestController {

	private Logger logger = LoggerFactory.getLogger(HomeRestController.class);

	@Autowired
	private ApplicationContext appContext;
	
	@Autowired
	private BookService bookService;


	@GetMapping("/books")
	public ResponseEntity<List<Book>> getBooks() throws IOException {
		List<Book> books = bookService.getAllBooks();
		return new ResponseEntity<List<Book>>(books, HttpStatus.OK);
	}
	
	@GetMapping("/books/{id}")
	public ResponseEntity<?> getABook(@PathVariable String id) throws IOException {
	    logger.info("getABook called with id: " + id);
        Optional<Book> aBook = bookService.getABook(id);

        if(aBook.isPresent()){
            return new ResponseEntity<Book>(aBook.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
	}

	@GetMapping("books/{id}/authors")
    public ResponseEntity<?> getAuthorsForBookById(@PathVariable String id) throws IOException {
	    logger.info("getAuthorsForBookById id: " + id);
        Optional<?> authorsOptional = bookService.getAuthorsForBookById(id);

        if(authorsOptional.isPresent()){
            List<String> authors = (List<String>) authorsOptional.get();
            return new ResponseEntity<List<String>>(authors, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
    }
}
