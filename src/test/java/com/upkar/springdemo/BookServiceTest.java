package com.upkar.springdemo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.upkar.springdemo.model.Book;
import com.upkar.springdemo.service.BookService;

@RunWith(SpringRunner.class)
@WebMvcTest(BookService.class)
public class BookServiceTest {
	private static final Logger logger = LoggerFactory.getLogger(BookServiceTest.class);

	 @Autowired
	private BookService bService;

	@Test
	public void testBookServiceExists() {
		assertNotNull(bService);
	}

	@Test
	public void testGetAllBooksReturnsListOfBooksExists() throws JsonParseException, JsonMappingException, IOException {
		List<Book> allBooks = bService.getAllBooks();
		assertNotNull(allBooks);
		assertThat(allBooks, instanceOf(List.class));
	}
}
