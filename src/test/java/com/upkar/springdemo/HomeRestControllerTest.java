package com.upkar.springdemo;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.upkar.springdemo.model.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.upkar.springdemo.controller.HomeRestController;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class HomeRestControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(HomeRestControllerTest.class);
	
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private TestRestTemplate restTemplate;


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
		mockMvc.perform(get(baseUrl + getAllBooksURL)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$", hasSize(4)))
				.andExpect(jsonPath("$[0].title", is("The Lightning Thief")));

//		restTemplate.getForEntity(baseUrl + getAllBooksURL, ResponseEntity[].class);

		ResponseEntity<Book[]> booksArray = restTemplate.getForEntity(baseUrl + getAllBooksURL, Book[].class);
		List<ResponseEntity<Book[]>> booksList = Arrays.asList(booksArray);

//		final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
//				MediaType.APPLICATION_JSON.getSubtype(),
//				Charset.forName("utf8")
//		);
//
//		mockMvc.perform(get("/api/todo"))
//				.andExpect(status().isOk())
//				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
//				.andExpect(jsonPath("$", hasSize(2)))
//				.andExpect(jsonPath("$[0].id", is(1)))
//				.andExpect(jsonPath("$[0].description", is("Lorem ipsum")))
//				.andExpect(jsonPath("$[0].title", is("Foo")))
//				.andExpect(jsonPath("$[1].id", is(2)))
//				.andExpect(jsonPath("$[1].description", is("Lorem ipsum")))
//				.andExpect(jsonPath("$[1].title", is("Bar")));

		logger.info("sample");
	}
	
	@Test
	public void getABookByIdShouldExist() throws Exception {
		mockMvc.perform(get(baseUrl + getABook).accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());
	}
	
	@Test
	public void invalidUrlMustReturn404() throws Exception{
		mockMvc.perform(get(invalidUrl).accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());
	}
}
