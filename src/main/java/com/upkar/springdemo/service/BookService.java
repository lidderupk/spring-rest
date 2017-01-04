package com.upkar.springdemo.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.upkar.springdemo.model.Book;
import com.upkar.springdemo.repository.BookRepository;
import com.upkar.springdemo.utils.ResourceInjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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

    private final BookRepository bRepo;

    @Autowired
    public BookService(BookRepository bRepo) throws IOException {
        this.bRepo = bRepo;
    }

    public List<Book> getAllBooks() throws IOException {
//        Book[] b = mapper.readValue(jsonFile, Book[].class);
        List<Book> list = new ArrayList<>();
        Iterable<Book> books = bRepo.findAll();
        Iterator<Book> iterator = books.iterator();
        iterator.forEachRemaining(list::add);
        return list;
    }

    public Optional<Book> getABook(final String id) throws IOException {
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

    /***
     * @param id
     * @return <pre>optional empty list if book does not have any authors
     * <pre>optional string list of authors if the book has one or more authors
     * <pre>empty optional if the book does not exist
     * @throws IOException
     */
    public Optional<?> getAuthorsForBookById(String id) throws IOException {
        List<Book> allBooks = getAllBooks();
        List<Book> filterBooks = allBooks.stream()
                .filter(b -> b.getId().equals(id))
                .collect(Collectors.toList());

        if (filterBooks.size() == 1) {
            return Optional.of(filterBooks.get(0).getAuthors());
        } else {
            return Optional.empty();
        }
    }

    public List<Book> getBooksGivenAuthorName(String authorName) throws IOException {
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
