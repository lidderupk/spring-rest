package com.upkar.springdemo;

import com.upkar.springdemo.model.Book;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by upkar on 11/29/16.
 */
public class BookCustomMatcher {
    public static Matcher<Book> hasTitle(final String title) {
        return new BaseMatcher<Book>() {
            @Override
            public boolean matches(Object item) {
                Book b = (Book)item;
                return b.getTitle().equalsIgnoreCase(title);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("hasTitle should return ").appendText(title);
            }
        };
    }

    public static Matcher<List<Book>> containsTitle(final String title){
        return new BaseMatcher<List<Book>>() {
            @Override
            public boolean matches(Object item) {
                List<Book> books = (List<Book>) item;
                List<Book> booksFiltered = books.stream()
                        .filter(b -> b.getTitle().equalsIgnoreCase(title))
                        .collect(Collectors.toList());
                return !booksFiltered.isEmpty();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("The books list does not contain ").appendText(title);
            }
        };
    }
}
