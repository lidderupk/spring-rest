package com.upkar.springdemo.service;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.upkar.springdemo.model.Book;

@Service
public class BookService {
	
	@Autowired
	ApplicationContext appContext;
	
	public List<Book> getAllBooks() throws JsonParseException, JsonMappingException, IOException{
		Resource resource = appContext.getResource("classpath:data.json");
		ObjectMapper mapper = new ObjectMapper();
		Book[] b = mapper.readValue(resource.getFile(), Book[].class);
		return Arrays.asList(b);
	}
}
