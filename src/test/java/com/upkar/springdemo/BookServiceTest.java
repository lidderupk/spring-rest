package com.upkar.springdemo;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
	public void testGetAllBooksReturnsListOfBooks() throws JsonParseException, JsonMappingException, IOException {
		List<Book> allBooks = bService.getAllBooks();
		assertNotNull(allBooks);
		assertThat(allBooks, instanceOf(List.class));
	}
	
	@Test
	public void testGetABookShouldReturnAnOptional() throws JsonParseException, JsonMappingException, IOException {
		String id = "doesnotexist";
		Optional<Book> result = bService.getABook(id);
		assertThat(result, isA(Optional.class));
	}
	
	@Test
	public void testGetABookShouldReturnEmptyOptionalIfBookDoesNotExist() throws JsonParseException, JsonMappingException, IOException {
		String id = "doesnotexist";
		Optional<Book> aBook = bService.getABook(id);
		assertThat(aBook.isPresent(), is(false));
	}
	
	@Test
	public void testGetABookShouldReturnBookOptionalIfBookExists() throws JsonParseException, JsonMappingException, IOException {
		String id = "978-0641723445";
		Optional<Book> aBook = bService.getABook(id);
		assertThat(aBook.isPresent(), is(true));
		assertThat(aBook.get().getId(), is(id));
	}
	
	//Test author request
	@Test
	public void getAuthorsGivenABookIdShouldReturnAList() throws JsonParseException, JsonMappingException, IOException {
		String id = "978-0641723445";
		List<String> authors = bService.getAuthorsForBookById(id);
		assertThat(authors, isA(List.class));
	}
	
	@Test
	public void getAuthorsGivenABookIdShouldReturnEmptyListForInvalidId() throws JsonParseException, JsonMappingException, IOException {
		String id = "doesnotexist";
		List<String> authors = bService.getAuthorsForBookById(id);
		assertThat(authors.isEmpty(), is(true));
	}
	
	@Test
	public void getAuthorsGivenBookIdShouldReturnListOfAuthors() throws JsonParseException, JsonMappingException, IOException {
		String id = "978-0641723445";
		List<String> authors = bService.getAuthorsForBookById(id);
		List<String> expectedAuthors = Arrays.asList(("Rick Riordan"));
		assertThat(authors, is(expectedAuthors));
	}
	
	//GET /books?searchByAuthor=text
	@Test
	public void getAllBooksGivenAuthorName() throws JsonParseException, JsonMappingException, IOException{
		String author = "Rick Riordan";
		List<Book> books = bService.getBooksGivenAuthorName(author);
		assertThat(books.isEmpty(), is(false));
		assertThat(books.size(), is(2));
		assertThat(books, BookCustomMatcher.containsTitle("The Lightning Thief"));
		assertThat(books, BookCustomMatcher.containsTitle("The Sea of Monsters"));
	}
}
