package com.upkar.springdemo.repository;

import com.upkar.springdemo.model.Book;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by upkar on 1/3/17.
 */
public interface BookRepository extends CrudRepository<Book, String> {
}
