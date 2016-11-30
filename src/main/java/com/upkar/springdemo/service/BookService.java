package com.upkar.springdemo.service;


import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.upkar.springdemo.model.Book;

@Service
public class BookService {

	/*
     * Test functionality:
	 * GET /books
	 * GET /books?offset=10&limit=5
	 * GET /books/{id}
	 * GET /books/{id}/{authors}
	 * GET /books?searchByAuthor=text
	 * GET /books?sort=ascending
	 * GET /books?sort=descending
	 */

    @Autowired
    ApplicationContext appContext;

    public List<Book> getAllBooks() throws JsonParseException, JsonMappingException, IOException {
        Resource resource = appContext.getResource("classpath:data.json");
        ObjectMapper mapper = new ObjectMapper();
        Book[] b = mapper.readValue(resource.getFile(), Book[].class);
        return Arrays.asList(b);
    }

    public Optional<Book> getABook(final String id) throws JsonParseException, JsonMappingException, IOException {
        List<Book> allBooks = getAllBooks();
        List<Book> searchResult = allBooks.stream()
                .filter(b -> b.getId().equalsIgnoreCase(id))
                .collect(Collectors.toList());

        //if nothing found or more than one books found, return the empty optional
        if (searchResult.size() != 1) {
            return Optional.empty();
        }
        return Optional.of(searchResult.get(0));
    }

    public List<String> getAuthorsForBookById(String id) throws JsonParseException, JsonMappingException, IOException {
        List<String> result = new ArrayList<>();
        List<Book> allBooks = getAllBooks();
        List<Book> filterBooks = allBooks.stream()
                .filter(b -> b.getId().equals(id))
                .collect(Collectors.toList());

        if (filterBooks.size() == 1) {
            result = filterBooks.get(0).getAuthors();
        }

        return result;
    }

    public List<Book> getBooksGivenAuthorName(String authorName) throws JsonParseException, JsonMappingException, IOException {
        List<Book> allBooks = getAllBooks();
        List<Book> filterByAuthor = allBooks.stream()
                .filter(b -> b.getAuthors().contains(authorName))
                .collect(Collectors.toList());
        return filterByAuthor;
    }

    public List<Book> getBooksByTitleAscending(boolean isAscending) throws IOException {
        List<Book> allBooks = getAllBooks();
        allBooks.sort(Comparator.comparing(b -> b.getTitle()));
        if(!isAscending) {
            Collections.reverse(allBooks);
        }
        return allBooks;
    }
}
