package com.upkar.springdemo.model;

import java.util.List;

/**
 * Created by upkar on 1/3/17.
 */
public class BookFactory {
    private String id;
    private String title;
    private String isbn;
    private String genre;
    private List<String> authors;
    private List<String> cat;

    public BookFactory(){

    }

    public BookFactory setNewId(String id){
        this.id = id;
        return this;
    }

    public BookFactory setNewTitle(String title){
        this.title = title;
        return this;
    }

    public BookFactory setNewISBN(String isbn){
        this.isbn = isbn;
        return this;
    }

    public BookFactory setnewGenre(String genre){
        this.genre = genre;
        return this;
    }

    public BookFactory setNewAuthors(List<String> authors){
        this.authors = authors;
        return this;
    }

    public BookFactory setNewCat(List<String> cat){
        this.cat = cat;
        return this;
    }

    public Book build(){
        return new Book(this.id, this.title, this.isbn, this.genre, this.authors, this.cat);
    }
}
