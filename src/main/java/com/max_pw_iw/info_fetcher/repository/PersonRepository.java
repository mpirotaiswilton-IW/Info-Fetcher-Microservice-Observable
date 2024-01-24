package com.max_pw_iw.info_fetcher.repository;

import org.springframework.data.repository.CrudRepository;

import com.max_pw_iw.info_fetcher.entity.Person;

public interface PersonRepository extends CrudRepository<Person, Long>{
}
