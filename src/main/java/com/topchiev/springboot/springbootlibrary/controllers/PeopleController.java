package com.topchiev.springboot.springbootlibrary.controllers;


import com.topchiev.springboot.springbootlibrary.models.Person;
import com.topchiev.springboot.springbootlibrary.services.PeopleService;
import com.topchiev.springboot.springbootlibrary.util.PersonValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PeopleService peopleService;
    private final PersonValidator personValidator;

    @Autowired
    public PeopleController(PeopleService peopleService, PersonValidator personValidator) {
        this.peopleService = peopleService;
        this.personValidator = personValidator;
    }

    @GetMapping()
    public String showPeople(Model model) {
        model.addAttribute("people", peopleService.findAll());
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

    @PatchMapping("/{id}/edit")
    public String updatePerson(@PathVariable("id") int id,
                               @ModelAttribute("person") @Valid Person person, BindingResult result) {
        if (result.hasErrors()) {
            return "/people/update_person";
        }
        peopleService.update(id, person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}/delete")
    public String deletePerson(@PathVariable("id") int id) {
        peopleService.delete(id);
        return "redirect:/people";
    }
}
