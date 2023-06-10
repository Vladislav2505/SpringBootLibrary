package com.topchiev.springboot.springbootlibrary.repositories;


import com.topchiev.springboot.springbootlibrary.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {
    List<Book> findByTitleStartingWith(String str);
}
