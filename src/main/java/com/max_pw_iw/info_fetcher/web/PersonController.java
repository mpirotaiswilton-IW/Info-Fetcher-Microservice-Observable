package com.max_pw_iw.info_fetcher.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.max_pw_iw.info_fetcher.entity.Person;
import com.max_pw_iw.info_fetcher.services.PersonService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/person")
public class PersonController {
    
    private PersonService personService;

    @GetMapping("/{id}")
	public ResponseEntity<Person> findById(@PathVariable Long id) {
		Person person = personService.getPerson(id);
		return new ResponseEntity<>(person, HttpStatus.OK);
	}

    @GetMapping("/")
	public ResponseEntity<List<Person>> findAll() {
		List<Person> people = personService.getPeople();
		return new ResponseEntity<>(people, HttpStatus.OK);
	}


}
