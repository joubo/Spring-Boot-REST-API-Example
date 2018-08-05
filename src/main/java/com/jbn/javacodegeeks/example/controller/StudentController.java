package com.jbn.javacodegeeks.example.controller;

import java.net.URI;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.jbn.javacodegeeks.example.model.Student;
import com.jbn.javacodegeeks.example.repository.StudentRepository;

@RestController
@RequestMapping("/students")
public class StudentController {
	private final StudentRepository repository;

	@Autowired
	public StudentController(StudentRepository repository) {
		this.repository = repository;
	}
	
	@Bean
	CommandLineRunner init(StudentRepository repository) {
		return args -> {
			repository.save(new Student("Jane", "Doe", "Junior"));
	        repository.save(new Student("Martin", "Fowler", "Senior"));
	        repository.save(new Student("Roy", "Fielding", "Freshman"));
		};
	}
	
	@GetMapping
	Collection <Student>readStundnts() {
		return this.repository.findAll();
	}
	
	@GetMapping("/{id}")
	Student readStudent(@PathVariable Long id) {
		return this.repository.findById(id)
				.orElseThrow(StudentNotFoundException::new);
	}
	
	@PostMapping
	ResponseEntity<Student> addStudent(@RequestBody Student student) {
		Student result = this.repository.save(student);
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(result.getId())
				.toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping
	Student updateStudent(@RequestBody Student student) {
		return this.repository.update(student)
				.orElseThrow(StudentNotFoundException::new);
	}
	
	@DeleteMapping("/{id}")
	void deleteStudent (@PathVariable Long id) {
		this.repository.delete(id)
			.orElseThrow(StudentNotFoundException::new);
	}
}
