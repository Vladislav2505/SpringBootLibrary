package com.topchiev.springboot.springbootlibrary.controllers;

import com.topchiev.springboot.springbootlibrary.models.Book;

import com.topchiev.springboot.springbootlibrary.models.Person;
import com.topchiev.springboot.springbootlibrary.services.BooksService;
import com.topchiev.springboot.springbootlibrary.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@Controller
@RequestMapping("/books")
public class BooksController {
    private final BooksService booksService;
    private final PeopleService peopleService;
    private static int pageCount = 0;

    @Autowired
    public BooksController(BooksService booksService, PeopleService peopleService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String showBooks(Model model,
                            @RequestParam(value = "sort_by_year", required = false) boolean sortByYear,
                            @RequestParam(value = "nextPage", required = false) boolean nextPage,
                            @RequestParam(value = "previousPage", required = false) boolean previousPage,
                            @RequestParam(value = "backToTop", required = false) boolean backToTop) {

        if (backToTop) {
            pageCount = 0;
            model.addAttribute("books", booksService.findWithPagination(pageCount, sortByYear));
        }
        else if (nextPage) {
            model.addAttribute("books", booksService.findWithPagination(++pageCount, sortByYear));
        } else if (previousPage && pageCount > 0) {
            model.addAttribute("books", booksService.findWithPagination(--pageCount, sortByYear));
        } else {
            model.addAttribute("books", booksService.findWithPagination(pageCount, sortByYear));
        }

        if (pageCount == 0)
            model.addAttribute("hidePrevious", false);
        else
            model.addAttribute("hidePrevious", true);

        if (booksService.findWithPagination(pageCount + 1, sortByYear).isEmpty())
            model.addAttribute("hideNext", false);
        else
            model.addAttribute("hideNext", true);

        return "/books/show_books";
    }

    @GetMapping("/{id}")
    public String showBook(@PathVariable("id") int id, Model model,
                           @ModelAttribute("person") Person person) {
        model.addAttribute("book", booksService.findOne(id));
        Person owner = booksService.findOne(id).getOwner();
        if (owner != null)
            model.addAttribute("owner", owner);
        else
            model.addAttribute("people", peopleService.findAll());

        return "/books/show_book";
    }

    @GetMapping("/new")
    public String createBook(@ModelAttribute("book") Book book) {
        return "/books/add_book";
    }

    @PostMapping("/new")
    public String addBook(@ModelAttribute("book") @Valid Book book, BindingResult result) {
        if (result.hasErrors())
            return "/books/add_book";

        booksService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String editBook(@PathVariable("id") int id, Model model) {
        Book book = booksService.findOne(id);
        model.addAttribute("book", book);
        return "/books/update_book";
    }

    @PostMapping("/{id}/edit")
    public String updateBook(@PathVariable("id") int id,
                             @ModelAttribute("book") @Valid Book book, BindingResult result) {
        if (result.hasErrors())
            return "/books/update_book";

        booksService.update(id, book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/delete")
    public String deleteBook(@PathVariable("id") int id) {
        booksService.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/release")
    public String releaseBook(@PathVariable("id") int id) {
        booksService.release(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/assign")
    public String assignBook(@PathVariable("id") int id, @ModelAttribute("person") Person person) {
        booksService.assign(id, person);
        return "redirect:/books";
    }

    @GetMapping("/search")
    public String searchBook() {
        return "books/search_books";
    }

    @PostMapping("/search")
    public String makeSearch(
            Model model, @RequestParam(value = "query", required = false) String query) {
        model.addAttribute("books", booksService.findByTitleStartingWith(query));
        return "books/search_books";
    }
}
