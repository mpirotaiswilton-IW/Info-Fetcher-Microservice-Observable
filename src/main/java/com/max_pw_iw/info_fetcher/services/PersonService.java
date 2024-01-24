package com.max_pw_iw.info_fetcher.services;

import java.util.List;

import com.max_pw_iw.info_fetcher.entity.Person;

public interface PersonService {
    Person getPerson(Long id);
    List<Person> getPeople();
    
}
