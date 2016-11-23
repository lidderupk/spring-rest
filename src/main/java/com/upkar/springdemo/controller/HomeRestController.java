package com.upkar.springdemo.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upkar.springdemo.model.Book;

@RestController
public class HomeRestController {

	private Logger logger = LoggerFactory.getLogger(HomeRestController.class);

	@Autowired
	private ApplicationContext appContext;


	@GetMapping("/books")
	public ResponseEntity<List<Book>> getBooks() throws IOException {
		Resource resource = appContext.getResource("classpath:data.json");
		ObjectMapper mapper = new ObjectMapper();
		Book[] b = mapper.readValue(resource.getFile(), Book[].class);
		return new ResponseEntity<List<Book>>(Arrays.asList(b), HttpStatus.OK);
	}
}
