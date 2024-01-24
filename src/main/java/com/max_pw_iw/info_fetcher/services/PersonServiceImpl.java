package com.max_pw_iw.info_fetcher.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.max_pw_iw.info_fetcher.entity.Person;
import com.max_pw_iw.info_fetcher.exception.PersonNotFoundException;
import com.max_pw_iw.info_fetcher.repository.PersonRepository;

import lombok.AllArgsConstructor;

@Service(value = "personService")
@AllArgsConstructor
public class PersonServiceImpl implements PersonService{

    private PersonRepository personRepository;

    @Override
    public Person getPerson(Long id) {
       Optional<Person> person = personRepository.findById(id);
       return unwrapPerson(person, id);
    }

    @Override
    public List<Person> getPeople() {
        return (List<Person>) personRepository.findAll();
    }

    static Person unwrapPerson(Optional<Person> entity, Long id) {
    if (entity.isPresent()) return entity.get();
    else throw new PersonNotFoundException(id);
    }
    
}
