package com.max_pw_iw.info_fetcher.exception;

public class PersonNotFoundException extends RuntimeException {
    public PersonNotFoundException(Long id){
        super("The Person Object with id '" + id + "' does not exist in our records");
    }
}
