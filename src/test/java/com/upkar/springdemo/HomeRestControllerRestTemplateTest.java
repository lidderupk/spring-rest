package com.upkar.springdemo;

import com.upkar.springdemo.model.Book;
import com.upkar.springdemo.utils.Constants;
import org.hamcrest.core.AllOf;
import org.hamcrest.core.IsEqual;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletResponse;
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
        assertThat(responseEntity.getStatusCode(),is(HttpStatus.OK));
//        assertThat(responseEntity.getHeaders().getContentType(), is(MediaType.APPLICATION_JSON_UTF8_VALUE));

//        mockMvc.perform(get(baseUrl + getAllBooksURL + "/" + id).accept(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(jsonPath("$").isNotEmpty())
//                .andExpect(jsonPath("$.title", is(expectedTitle)));
    }

}
