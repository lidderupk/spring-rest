package com.upkar.springdemo;

import com.jayway.jsonpath.JsonPath;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletResponse;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HomeRestControllerRestAssuredTest {

	private static final Logger logger = LoggerFactory.getLogger(HomeRestControllerRestAssuredTest.class);

    @Value("${local.server.port}")
    private int port;

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

    @Before
    public void setup() {
        logger.info("setting RestAssured port: " + port);
        RestAssured.baseURI = "http://localhost:" + port;
    }

    @Test
    public void getAllBooksShouldReturnListOfAllBooksWithRestAssured() throws Exception {

        final String expectedTitle = "The Lightning Thief";

        String body = given().
                when().
                    get(baseUrl + getAllBooksURL).
                then().
                    statusCode(HttpServletResponse.SC_OK).
                    contentType("application/json").
                    and().
                    body("$", hasSize(5)).
                    and().
                    body("title", Matchers.hasItem(expectedTitle)).
                    extract().body().asString();

        logger.info("body: " + body);
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
