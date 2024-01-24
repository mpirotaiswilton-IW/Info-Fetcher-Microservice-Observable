package com.max_pw_iw.info_fetcher;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.max_pw_iw.info_fetcher.entity.Person;
import com.max_pw_iw.info_fetcher.repository.PersonRepository;

import lombok.AllArgsConstructor;

@SpringBootApplication
@AllArgsConstructor
public class InfoFetcherApplication implements CommandLineRunner{

	private PersonRepository personRepository;

	public static void main(String[] args) {
		SpringApplication.run(InfoFetcherApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// List<Person> people = new ArrayList<Person>();

		// try (Scanner scanner = new Scanner(new File("/opt/app/data/Mock_data.csv"))) {
		// 	while (scanner.hasNextLine()) {
		// 		people.add(getPersonFromLine(scanner.nextLine()));
		// 	}
		// }

		// personRepository.saveAll(people);
	}


	private Person getPersonFromLine(String line) {
		Person value = new Person();
		String[] values = line.split(",");
		value.setId(Long.parseLong(values[0]));
		value.setFirstName(values[1]);
		value.setLastName(values[2]);
		value.setAge(Integer.parseInt(values[3]));
		value.setSex(values[4]);

		return value;
	}
}
