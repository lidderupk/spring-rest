package com.upkar.springdemo;

import com.upkar.springdemo.bootstrap.BookLoader;
import com.upkar.springdemo.controller.HomeRestController;
import com.upkar.springdemo.repository.BookRepository;
import com.upkar.springdemo.utils.Constants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(HomeRestController.class)
public class HomeRestControllerMockMVCTest {

    private static final Logger logger = LoggerFactory.getLogger(HomeRestControllerMockMVCTest.class);

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

    @Autowired
    private MockMvc mockMvc;

    //mock out the repository, needed by bookservice
    @MockBean
    BookRepository repo;

    @Before
    public void setup(){
        Mockito.when(repo.findAll()).thenReturn(BookLoader.loadBooks());
    }

    @Test
    public void getAllBooksShouldExist() throws Exception {
        mockMvc.perform(get(baseUrl + getAllBooksURL).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllBooksShouldReturnListOfAllBooks() throws Exception {

        final String expectedTitle = "The Lightning Thief";

        mockMvc.perform(get(baseUrl + getAllBooksURL)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].title", is(expectedTitle)));
    }

    @Test
    public void getABookByIdShouldExist() throws Exception {
        final String id = "978-0641723445";
        mockMvc.perform(get(baseUrl + getAllBooksURL + "/" + id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getABookShouldReturnBookIfExists() throws Exception {
        final String id = "978-0641723445";
        final String expectedTitle = "The Lightning Thief";
        mockMvc.perform(get(baseUrl + getAllBooksURL + "/" + id).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.title", is(expectedTitle)));
    }

    @Test
    public void getABookShouldReturnNotFoundIfBookDoesNotExist() throws Exception {
        final String id = "doesnotexist";
        mockMvc.perform(get(baseUrl + getAllBooksURL + "/" + id).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    public void invalidUrlMustReturn404() throws Exception {
        mockMvc.perform(get(invalidUrl).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    //Test author request - /books/{id}/{authors}
    @Test
    public void getAuthorsForBookByIdShouldExist() throws Exception {
        final String id = "978-0641723445";

        mockMvc.perform(get(baseUrl + getAllBooksURL + "/" + id + Constants.authorUrl).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void getAuthorsForBookByIdShouldReturn404IfBookNotFound() throws Exception {
        mockMvc.perform(get(baseUrl + getAllBooksURL + "/" + Constants.doesNotExist + Constants.authorUrl).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getAuthorsForBookByIdShouldReturnListOfAuthors() throws Exception {
        String id = "978-0641723445";
        final String expectedAuthor = "Rick Riordan";
        mockMvc.perform(get(baseUrl + getAllBooksURL + "/" + id + Constants.authorUrl).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]", is(expectedAuthor)));

        id = "978-1933988177";
        final List<String> expectedAuthors = Arrays.asList("Michael McCandless", "Erik Hatcher", "Otis Gospodnetic");
        mockMvc.perform(get(baseUrl + getAllBooksURL + "/" + id + Constants.authorUrl).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$", is(expectedAuthors)));
    }

    @Test
    public void getAuthorsForBookByIdShouldReturnEmptyArrayIfNoAuthors() throws Exception {
        final String id = "978-1935589679";
        mockMvc.perform(get(baseUrl + getAllBooksURL + "/" + id + Constants.authorUrl).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void getBooksByTitleAscendingShouldReturnBooksAscendingByTitle() throws Exception {

        String url = baseUrl + getAllBooksURL + "?sort=ascending";


        List<String> sortedTitles = Arrays.asList("The Lightning Thief", "The Sea of Monsters"
                , "Sophie's World : The Greek Philosophers", "Lucene in Action, Second Edition", "A Guide to the Project Management Body of Knowledge");
        Collections.sort(sortedTitles);

        MvcResult mvcResult = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].title", is(sortedTitles.get(0))))
                .andReturn();
    }

    @Test
    public void getBooksByTitleDescendingShouldReturnBooksDesendingByTitle() throws Exception {

        String url = baseUrl + getAllBooksURL + "?sort=descending";


        List<String> sortedTitles = Arrays.asList("The Lightning Thief", "The Sea of Monsters"
                , "Sophie's World : The Greek Philosophers", "Lucene in Action, Second Edition", "A Guide to the Project Management Body of Knowledge");
        Collections.sort(sortedTitles);
        Collections.reverse(sortedTitles);

        MvcResult mvcResult = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].title", is(sortedTitles.get(0))))
                .andReturn();

        logger.info("debug");
    }
}
