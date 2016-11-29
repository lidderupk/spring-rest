package com.upkar.springdemo.controller;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
	public ResponseEntity<Book> getABook() throws IOException {
		return new ResponseEntity<Book>(new Book(), HttpStatus.NOT_FOUND);
	}
}
