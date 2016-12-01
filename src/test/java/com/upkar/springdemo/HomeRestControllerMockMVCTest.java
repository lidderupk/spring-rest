package com.upkar.springdemo;

import com.upkar.springdemo.controller.HomeRestController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(HomeRestController.class)
public class HomeRestControllerMockMVCTest {

	private static final Logger logger = LoggerFactory.getLogger(HomeRestControllerMockMVCTest.class);

	@Autowired
	private MockMvc mockMvc;

	/*
	 * Test functionality:
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
	private final String invalidUrl = "/books/hello/1";
	private final String getAnInvaludBook = "/books/9999999999999999";
	
	@Test
	public void getAllBooksShouldExist() throws Exception{
		mockMvc.perform(get(baseUrl+getAllBooksURL).accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}

	@Test
	public void getAllBooksShouldReturnListOfAllBooks() throws Exception {

		final String expectedTitle = "The Lightning Thief";

		mockMvc.perform(get(baseUrl + getAllBooksURL)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$", hasSize(4)))
				.andExpect(jsonPath("$[0].title", is(expectedTitle)));
	}

	
//	@Test
//	public void getABookByIdShouldExist() throws Exception {
//		mockMvc.perform(get(baseUrl + getABook).accept(MediaType.APPLICATION_JSON))
//		.andExpect(status().isNotFound());
//	}
//
//	@Test
//	public void invalidUrlMustReturn404() throws Exception{
//		mockMvc.perform(get(invalidUrl).accept(MediaType.APPLICATION_JSON))
//		.andExpect(status().isNotFound());
//	}
}
