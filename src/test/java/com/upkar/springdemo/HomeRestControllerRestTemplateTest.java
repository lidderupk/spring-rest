package com.upkar.springdemo;

import com.upkar.springdemo.model.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HomeRestControllerRestTemplateTest {

	private static final Logger logger = LoggerFactory.getLogger(HomeRestControllerRestTemplateTest.class);

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
	public void getAllBooksShouldReturnListOfAllBooks() throws Exception {

		final String expectedTitle = "The Lightning Thief";

		//another way to test
		ResponseEntity<Book[]> responseEntity = restTemplate.getForEntity(baseUrl + getAllBooksURL, Book[].class);
		Book[] books = responseEntity.getBody();
		List<Book> booksList = Arrays.asList(books);
		assertThat(booksList.isEmpty(), is(false));
		assertThat(booksList.size(), is(5));
		assertThat(booksList.get(0).getTitle(), is(expectedTitle));
	}
}
