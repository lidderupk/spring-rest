package com.upkar.springdemo;

import com.upkar.springdemo.bootstrap.BookLoader;
import com.upkar.springdemo.model.Book;
import com.upkar.springdemo.repository.BookRepository;
import com.upkar.springdemo.service.BookService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@WebMvcTest(BookService.class)
public class BookServiceTest {
	private static final Logger logger = LoggerFactory.getLogger(BookServiceTest.class);
	private final String doesnotexist = "doesnotexist";

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



	@Autowired
	private BookService bService;

	//mock out the repository, needed by bookservice
    @MockBean
    BookRepository repo;

    @Before
    public void setup(){
        Mockito.when(repo.findAll()).thenReturn(BookLoader.loadBooks());
    }

	@Test
	public void bookServiceShouldExist() {
		assertNotNull(bService);
	}

	@Test
	public void getAllBooksShouldReturnListOfBooks() throws IOException {
		List<Book> allBooks = bService.getAllBooks();
		assertNotNull(allBooks);
		assertThat(allBooks, instanceOf(List.class));
	}
	
	@Test
	public void getABookShouldReturnOptional() throws IOException {
		Optional<Book> result = bService.getABook(doesnotexist);
		assertThat(result, isA(Optional.class));
	}
	
	@Test
	public void getABookShouldReturnEmptyOptionalIfBookDoesNotExist() throws IOException {
		Optional<Book> aBook = bService.getABook(doesnotexist);
		assertThat(aBook.isPresent(), is(false));
	}
	
	@Test
	public void getABookShouldReturnBookOptionalIfBookExists() throws IOException {
		String id = "978-0641723445";
		Optional<Book> aBook = bService.getABook(id);
		assertThat(aBook.isPresent(), is(true));
		assertThat(aBook.get().getId(), is(id));
	}
	
	//Test author request
	@Test
	public void getAuthorsForBookByIdShouldReturnAnOptional() throws IOException {
		String id = "978-0641723445";
        Optional<?> authorsOptional = bService.getAuthorsForBookById(id);

        //is this a valid test? I am casting to list and testing if it is a list. Seems redundant
        if(authorsOptional.isPresent()) {
            assertThat((List<String>)authorsOptional.get(), isA(List.class));
        }
	}
	
	@Test
	public void getAuthorsForBookByIdShouldReturnEmptyOptionalForInvalidId() throws IOException {
        Optional<?> authorsOptional = bService.getAuthorsForBookById(doesnotexist);
        assertThat(authorsOptional.isPresent(), is(false));
	}
	
	@Test
	public void getAuthorsForBookByIdShouldReturnListOfAuthors() throws IOException {
		String id = "978-0641723445";
        Optional<?> authorsOptional = bService.getAuthorsForBookById(id);
        List<String> expectedAuthors = Arrays.asList(("Rick Riordan"));
        assertThat(authorsOptional.isPresent(), is(true));
		assertThat(authorsOptional.get(), is(expectedAuthors));
	}
	
	//GET /books?searchByAuthor=text
	@Test
	public void getBooksGivenAuthorNameShouldReturnAllBooksByAuthor() throws IOException{
		String author = "Rick Riordan";
		List<Book> books = bService.getBooksGivenAuthorName(author);
		assertThat(books.isEmpty(), is(false));
		assertThat(books.size(), is(2));
		assertThat(books, BookCustomMatcher.containsTitle("The Lightning Thief"));
		assertThat(books, BookCustomMatcher.containsTitle("The Sea of Monsters"));
	}

	@Test
	public void getBooksByTitleAscendingShouldReturnBooksAscendingByTitle() throws IOException {
		List<Book> result = bService.getBooksByTitleAscending(true);
		List<String> sortedTitles = Arrays.asList("The Lightning Thief", "The Sea of Monsters"
				, "Sophie's World : The Greek Philosophers", "Lucene in Action, Second Edition", "A Guide to the Project Management Body of Knowledge");
		Collections.sort(sortedTitles);

		List<String> resultTitles = result.stream()
								.map(b -> b.getTitle())
								.collect(Collectors.toList());

		assertThat(resultTitles, is(sortedTitles));
	}

	@Test
	public void getBooksByTitleDescendingShouldReturnBooksDescendingByTitle() throws IOException {
		List<Book> result = bService.getBooksByTitleAscending(false);
		List<String> sortedTitles = Arrays.asList("The Lightning Thief", "The Sea of Monsters"
				, "Sophie's World : The Greek Philosophers", "Lucene in Action, Second Edition", "A Guide to the Project Management Body of Knowledge");
		Collections.sort(sortedTitles);
		Collections.reverse(sortedTitles);

		List<String> resultTitles = result.stream()
				.map(b -> b.getTitle())
				.collect(Collectors.toList());

		assertThat(resultTitles, is(sortedTitles));
	}
}
