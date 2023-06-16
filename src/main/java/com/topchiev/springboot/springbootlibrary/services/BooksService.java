package com.topchiev.springboot.springbootlibrary.services;


import com.topchiev.springboot.springbootlibrary.models.Book;
import com.topchiev.springboot.springbootlibrary.models.Person;
import com.topchiev.springboot.springbootlibrary.repositories.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {
    public final BooksRepository booksRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> findAll(boolean sortByYear) {
        if (sortByYear)
            return booksRepository.findAll(Sort.by("year"));
        else
            return booksRepository.findAll();
    }

    public Book findOne(int id) {
        Optional<Book> foundPerson = booksRepository.findById(id);
        return foundPerson.orElse(null);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book book) {
        book.setId(id);
        booksRepository.save(book);
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    @Transactional
    public void release(int id) {
        booksRepository.findById(id).ifPresent(book -> {
            book.setOwner(null);
        });
    }

    @Transactional
    public void assign(int id, Person person) {
        booksRepository.findById(id).ifPresent(book -> {
            book.setOwner(person);
        });
    }

    public List<Book> findWithPagination(Integer page, boolean sortByYear) {
        if (sortByYear)
            return booksRepository.
                    findAll(PageRequest.of(page, 6, Sort.by("year"))).getContent();
        else
            return booksRepository.findAll(PageRequest.of(page, 6)).getContent();
    }

    public List<Book> findByTitleStartingWith(String str){
        if (str.isEmpty())
            return Collections.emptyList();
        return booksRepository.findByTitleStartingWith(str);
    }
}
