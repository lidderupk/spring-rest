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

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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
	    mockMvc.perform(get(baseUrl+getAllBooksURL+"/"+id).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

	@Test
	public void invalidUrlMustReturn404() throws Exception{
		mockMvc.perform(get(invalidUrl).accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());
	}

    //Test author request - /books/{id}/{authors}
    @Test
    public void getAuthorsForBookByIdShouldExist() throws Exception {
        final String id = "978-0641723445";
        mockMvc.perform(get(baseUrl+getAllBooksURL+"/"+id+"/authors").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void getAuthorsForBookByIdShouldReturn404IfBookNotFound() throws Exception {
        final String id = "doesnotexist";
        mockMvc.perform(get(baseUrl+getAllBooksURL+"/"+id+"/authors").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getAuthorsForBookByIdShouldReturnListOfAuthors() throws Exception {
        String id = "978-0641723445";
        final String expectedAuthor = "Rick Riordan";
        mockMvc.perform(get(baseUrl + getAllBooksURL + "/" + id + "/authors").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]", is(expectedAuthor)));

        id = "978-1933988177";
        final List<String> expectedAuthors = Arrays.asList("Michael McCandless", "Erik Hatcher", "Otis Gospodnetic");
        mockMvc.perform(get(baseUrl + getAllBooksURL + "/" + id + "/authors").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$", is(expectedAuthors)));
    }

    @Test
    public void getAuthorsForBookByIdShouldReturnEmptyArrayIfNoAuthors() throws Exception {
	    final String id = "978-1935589679";
	    mockMvc.perform(get(baseUrl+getAllBooksURL+"/" + id + "/authors").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isEmpty());
    }
}
