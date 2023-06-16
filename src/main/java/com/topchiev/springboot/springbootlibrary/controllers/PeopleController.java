package com.topchiev.springboot.springbootlibrary.controllers;


import com.topchiev.springboot.springbootlibrary.models.Person;
import com.topchiev.springboot.springbootlibrary.services.PeopleService;
import com.topchiev.springboot.springbootlibrary.util.PersonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PeopleService peopleService;
    private final PersonValidator personValidator;
    private static int pageCount = 0;

    @Autowired
    public PeopleController(PeopleService peopleService, PersonValidator personValidator) {
        this.peopleService = peopleService;
        this.personValidator = personValidator;
    }

    @GetMapping()
    public String showPeople(Model model,
                             @RequestParam(value = "nextPage", required = false) boolean nextPage,
                             @RequestParam(value = "previousPage", required = false) boolean previousPage,
                             @RequestParam(value = "backToTop", required = false) boolean backToTop) {
        if (backToTop) {
            pageCount = 0;
            model.addAttribute("people", peopleService.findWithPagination(pageCount));
        }
        else if (nextPage) {
            model.addAttribute("people", peopleService.findWithPagination(++pageCount));
        } else if (previousPage && pageCount > 0) {
            model.addAttribute("people", peopleService.findWithPagination(--pageCount));
        } else {
            model.addAttribute("people", peopleService.findWithPagination(pageCount));
        }

        if (pageCount == 0)
            model.addAttribute("hidePrevious", false);
        else
            model.addAttribute("hidePrevious", true);

        if (peopleService.findWithPagination(pageCount + 1).isEmpty())
            model.addAttribute("hideNext", false);
        else
            model.addAttribute("hideNext", true);

        return "/people/show_people";
    }

    @GetMapping("/{id}")
    public String showPerson(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", peopleService.findOne(id));
        model.addAttribute("books", peopleService.getBooksByPersonId(id));
        return "/people/show_person";
    }

    @GetMapping("/new")
    public String createPerson(@ModelAttribute("person") Person person) {
        return "/people/add_person";
    }

    @PostMapping("/new")
    public String addPerson(@ModelAttribute("person") @Valid Person person, BindingResult result) {

        personValidator.validate(person, result);
        if (result.hasErrors()) {
            return "/people/add_person";
        }
        peopleService.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String editPerson(@PathVariable("id") int id, Model model) {
        Person person = peopleService.findOne(id);
        model.addAttribute("person", person);
        return "/people/update_person";
    }

    @PostMapping("/{id}/edit")
    public String updatePerson(@PathVariable("id") int id,
                               @ModelAttribute("person") @Valid Person person, BindingResult result) {
        if (result.hasErrors()) {
            return "/people/update_person";
        }
        peopleService.update(id, person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/delete")
    public String deletePerson(@PathVariable("id") int id) {
        peopleService.delete(id);
        return "redirect:/people";
    }
}
