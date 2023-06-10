package com.topchiev.springboot.springbootlibrary.controllers;

import com.topchiev.springboot.springbootlibrary.models.Book;

import com.topchiev.springboot.springbootlibrary.models.Person;
import com.topchiev.springboot.springbootlibrary.services.BooksService;
import com.topchiev.springboot.springbootlibrary.services.PeopleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/books")
public class BooksController {
    private final BooksService booksService;
    private final PeopleService peopleService;

    @Autowired
    public BooksController(BooksService booksService, PeopleService peopleService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String showBooks(Model model,
                            @RequestParam(value = "sort_by_year", required = false) boolean sortByYear,
                            @RequestParam(value = "page", required = false) Integer page,
                            @RequestParam(value = "books_per_page", required = false) Integer booksPerPage) {
        if(page == null || booksPerPage == null)
            model.addAttribute("books", booksService.findAll(sortByYear));
        else
            model.addAttribute("books", booksService.findWithPagination(page, booksPerPage, sortByYear));
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

    @PatchMapping("/{id}/edit")
    public String updateBook(@PathVariable("id") int id,
                             @ModelAttribute("book") @Valid Book book, BindingResult result) {
        if (result.hasErrors())
            return "/books/update_book";

        booksService.update(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}/delete")
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
    public String searchBook(){
        return "books/search_books";
    }

    @PostMapping("/search")
    public String makeSearch(
            Model model, @RequestParam(value = "query", required = false) String query){
        model.addAttribute("books", booksService.findByTitleStartingWith(query));
        return "books/search_books";
    }
}
