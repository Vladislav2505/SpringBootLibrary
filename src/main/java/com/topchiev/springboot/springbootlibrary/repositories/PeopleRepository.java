package com.topchiev.springboot.springbootlibrary.repositories;

import com.topchiev.springboot.springbootlibrary.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByFullName(String fullName);
}
