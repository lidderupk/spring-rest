package com.upkar.springdemo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.upkar.springdemo.controller.HomeRestController;

@RunWith(SpringRunner.class)
@WebMvcTest(HomeRestController.class)
public class HomeRestControllerTest {
	private static final Logger logger = LoggerFactory.getLogger(HomeRestControllerTest.class);
	
	@Autowired
	private MockMvc mockMvc;
	
	/*
	 * test
	 * API version /v1/
	 * GET /books
	 * GET /books?offset=10&limit=5
	 * GET /books/{id}
	 * GET /books/{id}/{authors}
	 * GET /books?searchByAuthor=text
	 * GET /books?sort=ascending
	 * GET /books?sort=descending
	 */
	
	private final String baseUrl = "/api";
	private final String getAllBooksURL = "/books";
	private final String getABook = "/books/1";
	private final String getAnInvaludBook = "/books/9999999999999999";
	
	@Test
	public void getAllBooksShouldExist() throws Exception{
		mockMvc.perform(get(baseUrl+getAllBooksURL).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}
}
