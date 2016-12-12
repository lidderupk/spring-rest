package com.upkar.springdemo.controller;

import com.upkar.springdemo.model.Book;
import com.upkar.springdemo.service.BookService;
import com.upkar.springdemo.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping(Constants.apiBaseURL)
public class HomeRestController {

	private final Logger logger = LoggerFactory.getLogger(HomeRestController.class);
	private final ApplicationContext appContext;
	private final BookService bookService;

     @Autowired
     public HomeRestController(ApplicationContext app, BookService bService){
         this.appContext = app;
         this.bookService = bService;
     }

	@GetMapping(
	        value={Constants.apiAllBooksURL},
            params = Constants.apiAllBooksURLParamSort
    )
	public ResponseEntity<List<Book>> getBooksSort(@RequestParam("sort") String sort) throws IOException {
        logger.info("getABook called with sort: " + sort);
		List<Book> books = bookService.getAllBooks();

		if(sort != null && sort.equalsIgnoreCase("ascending")){
		    Collections.sort(books);
        } else if(sort != null && sort.equalsIgnoreCase("descending")) {
            Collections.sort(books);
            Collections.reverse(books);
        }
		return new ResponseEntity<List<Book>>(books, HttpStatus.OK);
	}

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
