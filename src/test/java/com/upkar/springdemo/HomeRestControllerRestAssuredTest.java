package com.upkar.springdemo;

import com.upkar.springdemo.utils.Constants;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsArray;
import org.hamcrest.collection.IsArrayContaining;
import org.hamcrest.collection.IsEmptyCollection;
import org.hamcrest.text.IsEmptyString;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.soap.MTOM;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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

    @Test
    public void getABookShouldReturnBookIfExists() throws Exception {
        final String id = "978-0641723445";
        final String expectedTitle = "The Lightning Thief";

        String body = given().when().
                get(baseUrl + getAllBooksURL + "/" + id).
                then().
                statusCode(HttpServletResponse.SC_OK).
                contentType(ContentType.JSON).
                and().
                body("title", Matchers.is(expectedTitle)).
                extract().body().asString();

        logger.info("body: " + body);
    }

    @Test
    public void getABookShouldReturnNotFoundIfBookDoesNotExist() throws Exception {
        final String id = "doesnotexist";
        given().when()
                .get(baseUrl + getAllBooksURL + "/" + id).
                then().
                statusCode(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    public void invalidUrlMustReturn404() throws Exception {
        given().when()
                .accept(ContentType.JSON)
                .get(Constants.invalidUrl)
                .then()
                .statusCode(HttpServletResponse.SC_NOT_FOUND);
    }

    //Test author request - /books/{id}/{authors}
    @Test
    public void getAuthorsForBookByIdShouldExist() throws Exception {
        final String id = "978-0641723445";
        given().when()
                .accept(ContentType.JSON)
                .get(Constants.apiBaseURL + Constants.apiAllBooksURL + "/" + id)
                .then()
                .statusCode(HttpServletResponse.SC_OK);
    }

    @Test
    public void getAuthorsForBookByIdShouldReturn404IfBookNotFound() throws Exception {
        given().when()
                .accept(ContentType.JSON)
                .get(Constants.apiBaseURL + Constants.apiAllBooksURL + "/" + Constants.doesNotExist + Constants.authorUrl)
                .then()
                .statusCode(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    public void getAuthorsForBookByIdShouldReturnListOfAuthors() throws Exception {
        String id = "978-0641723445";
        final String expectedAuthor = "Rick Riordan";

        String body = given().when()
                .accept(ContentType.JSON)
                .get(Constants.apiBaseURL + Constants.apiAllBooksURL + "/" + id + Constants.authorUrl)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpServletResponse.SC_OK)
                .body("size()", greaterThan(0))
                .body("$", hasSize(1))
                .body("[0]", is(expectedAuthor))
                .extract().body().asString();

        logger.info(body);

        id = "978-1933988177";
        final String[] expectedAuthors = {"Michael McCandless", "Erik Hatcher", "Otis Gospodnetic"};

        body = given().when()
                .accept(ContentType.JSON)
                .get(Constants.apiBaseURL + Constants.apiAllBooksURL + "/" + id + Constants.authorUrl)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpServletResponse.SC_OK)
                .body("size()", greaterThan(0))
                .body("$", hasSize(3))
                .body("$", containsInAnyOrder(expectedAuthors))
                .extract().body().asString();

        logger.info("body: " + body);

        //another way to test arrays is to convert body into java iterable and then use assertThat.
        List<String> result = given().when()
                .accept(ContentType.JSON)
                .get(Constants.apiBaseURL + Constants.apiAllBooksURL + "/" + id + Constants.authorUrl)
                .jsonPath().getList("$");

        MatcherAssert.assertThat(result.toArray(), is(expectedAuthors));
    }

    @Test
    public void getAuthorsForBookByIdShouldReturnEmptyArrayIfNoAuthors() throws Exception {
        final String id = "978-1935589679";

        given().when()
                    .accept(ContentType.JSON)
                    .get(Constants.apiBaseURL + Constants.apiAllBooksURL + "/" + id + Constants.authorUrl)
                .then()
                    .contentType(ContentType.JSON)
                    .statusCode(HttpServletResponse.SC_OK)
                    .body("$", hasSize(0))
                    .body("$", emptyCollectionOf(String.class));
    }

    @Test
    public void getBooksByTitleAscendingShouldReturnBooksAscendingByTitle() throws Exception {

        String url = Constants.apiBaseURL + Constants.apiAllBooksURL + Constants.sortAscendingParam;


        List<String> sortedTitles = Arrays.asList("The Lightning Thief", "The Sea of Monsters"
                , "Sophie's World : The Greek Philosophers", "Lucene in Action, Second Edition", "A Guide to the Project Management Body of Knowledge");
        Collections.sort(sortedTitles);

        given().when()
                .accept(ContentType.JSON)
                .get(url)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpServletResponse.SC_OK)
                .body("$", not(empty()))
                .body("title", not(empty()))
                .body("title", equalTo(sortedTitles));

        //using jsonPath
        List<String>titles = given().when()
                .accept(ContentType.JSON)
                .get(url)
                .jsonPath()
                .getList("title");

        MatcherAssert.assertThat(titles, not(empty()));
        MatcherAssert.assertThat(titles, is(sortedTitles));
    }

    @Test
    public void getBooksByTitleDescendingShouldReturnBooksDesendingByTitle() throws Exception {

        String url = Constants.apiBaseURL + Constants.apiAllBooksURL + Constants.sortDescendingParam;


        List<String> sortedTitles = Arrays.asList("The Lightning Thief", "The Sea of Monsters"
                , "Sophie's World : The Greek Philosophers", "Lucene in Action, Second Edition", "A Guide to the Project Management Body of Knowledge");
        Collections.sort(sortedTitles);
        Collections.reverse(sortedTitles);

        given().when()
                .accept(ContentType.JSON)
                .get(url)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpServletResponse.SC_OK)
                .body("$", not(empty()))
                .body("title", not(empty()))
                .body("title", equalTo(sortedTitles));

        logger.info("debug");
    }
}
