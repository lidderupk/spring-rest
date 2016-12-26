package com.upkar.springdemo;

import com.upkar.springdemo.model.Book;
import com.upkar.springdemo.utils.Constants;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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

    @Test
    public void getAllBooksShouldExist() throws Exception {
        ResponseEntity<Book[]> resp = restTemplate.getForEntity(Constants.apiBaseURL + Constants.apiAllBooksURL, Book[].class);
        assertThat(resp.getStatusCode(), IsEqual.equalTo(HttpStatus.OK));
    }

    @Test
    public void getAllBooksShouldReturnListOfAllBooks() throws Exception {

        final String expectedTitle = "The Lightning Thief";

        //another way to test
        ResponseEntity<Book[]> responseEntity = restTemplate.getForEntity(Constants.apiBaseURL + Constants.apiAllBooksURL, Book[].class);
        Book[] books = responseEntity.getBody();
        List<Book> booksList = Arrays.asList(books);
        assertThat(booksList.isEmpty(), is(false));
        assertThat(booksList.size(), is(5));
        assertThat(booksList.get(0).getTitle(), is(expectedTitle));
    }

    @Test
    public void getABookByIdShouldExist() throws Exception {
        final String id = "978-0641723445";
        ResponseEntity<Book> responseEntity = restTemplate.getForEntity(Constants.apiBaseURL + Constants.apiAllBooksURL + "/" + id, Book.class);
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void getABookShouldReturnBookIfExists() throws Exception {
        final String id = "978-0641723445";
        final String expectedTitle = "The Lightning Thief";
        final String url = Constants.apiBaseURL + Constants.apiAllBooksURL + "/" + id;

        ResponseEntity<Book> responseEntity = restTemplate.getForEntity(url, Book.class);
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        Book book = responseEntity.getBody();
        assertThat(book, IsNull.notNullValue());
        assertThat(book.getTitle(), is(expectedTitle));
    }

    @Test
    public void getABookShouldReturnNotFoundIfBookDoesNotExist() throws Exception {
        final String id = "doesnotexist";
        ResponseEntity<Book> responseEntity = restTemplate.getForEntity(Constants.apiBaseURL + Constants.apiAllBooksURL + "/" + id, Book.class);
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void invalidUrlMustReturn404() throws Exception {
        ResponseEntity<Book> responseEntity = restTemplate.getForEntity(Constants.invalidUrl, Book.class);
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    //Test author request - /books/{id}/{authors}
    @Test
    public void getAuthorsForBookByIdShouldExist() throws Exception {
        final String id = "978-0641723445";

        ResponseEntity<String[]> responseEntity = restTemplate.getForEntity(Constants.apiBaseURL + Constants.apiAllBooksURL + "/" + id + Constants.authorUrl, String[].class);
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void getAuthorsForBookByIdShouldReturn404IfBookNotFound() throws Exception {

        ResponseEntity<String[]> responseEntity = restTemplate.getForEntity(Constants.apiBaseURL + Constants.apiAllBooksURL + "/" + Constants.doesNotExist + Constants.authorUrl, String[].class);
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void getAuthorsForBookByIdShouldReturnListOfAuthors() throws Exception {
        String id = "978-0641723445";
        final String expectedAuthor = "Rick Riordan";
        ResponseEntity<String[]> responseEntity = restTemplate.getForEntity(Constants.apiBaseURL + Constants.apiAllBooksURL + "/" + id + Constants.authorUrl, String[].class);
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        String[] authors = responseEntity.getBody();
        assertThat(authors.length, is(1));
        assertThat(authors[0], is(expectedAuthor));

        id = "978-1933988177";
        final List<String> expectedAuthors = Arrays.asList("Michael McCandless", "Erik Hatcher", "Otis Gospodnetic");
        responseEntity = restTemplate.getForEntity(Constants.apiBaseURL + Constants.apiAllBooksURL + "/" + id + Constants.authorUrl, String[].class);
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        authors = responseEntity.getBody();
        assertThat(authors.length, is(3));
        assertThat(Arrays.asList(authors), is(expectedAuthors));
    }

    @Test
    public void getAuthorsForBookByIdShouldReturnEmptyArrayIfNoAuthors() throws Exception {
        final String id = "978-1935589679";
        ResponseEntity<String[]> responseEntity = restTemplate.getForEntity(Constants.apiBaseURL + Constants.apiAllBooksURL + "/" + id + Constants.authorUrl, String[].class);
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        String[] authors = responseEntity.getBody();
        assertThat(authors.length, is(0));
    }

    @Test
    public void getBooksByTitleAscendingShouldReturnBooksAscendingByTitle() throws Exception {

        String url = Constants.apiBaseURL + Constants.apiAllBooksURL + "?sort=ascending";
        List<String> sortedTitles = Arrays.asList("The Lightning Thief", "The Sea of Monsters"
                , "Sophie's World : The Greek Philosophers", "Lucene in Action, Second Edition", "A Guide to the Project Management Body of Knowledge");
        Collections.sort(sortedTitles);

        ResponseEntity<Book[]> responseEntity = restTemplate.getForEntity(url, Book[].class);
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        Book[] books = responseEntity.getBody();
        assertThat(books.length, is(5));
        List<String> titles = Arrays.asList(books).parallelStream().map(b -> b.getTitle()).collect(Collectors.toList());
        assertThat(titles, is(sortedTitles));
    }

    @Test
    public void getBooksByTitleDescendingShouldReturnBooksDesendingByTitle() throws Exception {

        String url = Constants.apiBaseURL + Constants.apiAllBooksURL + "?sort=descending";
        List<String> sortedTitles = Arrays.asList("The Lightning Thief", "The Sea of Monsters"
                , "Sophie's World : The Greek Philosophers", "Lucene in Action, Second Edition", "A Guide to the Project Management Body of Knowledge");
        Collections.sort(sortedTitles);
        Collections.reverse(sortedTitles);

        ResponseEntity<Book[]> responseEntity = restTemplate.getForEntity(url, Book[].class);
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        Book[] books = responseEntity.getBody();
        assertThat(books.length, is(5));
        List<String> titles = Arrays.asList(books).parallelStream().map(b -> b.getTitle()).collect(Collectors.toList());
        assertThat(titles, is(sortedTitles));
    }

}
