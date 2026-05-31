package com.noname.todo.controller;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.noname.todo.entity.Todo;

@RestController
@RequestMapping("/todos")
public class TodoController {

	private List<Todo> todos = new ArrayList<>();

	@GetMapping
	public ResponseEntity<List<Todo>> getAll() {
		return ResponseEntity.ok(todos);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Todo> getById(@PathVariable("id") int id) {
		Optional<Todo> optTodo = todos.stream()
									.filter(todo -> todo.getId() == id)
									.findFirst();

		return optTodo
				.map(todo -> ResponseEntity.ok(todo))
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Void> addTodo(@RequestBody Todo todo) {
		todos.add(todo);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable("id") int id) {
		var isDeleted = todos.removeIf(todo -> Objects.equals(todo.getId(), id));

		return isDeleted 
			? ResponseEntity.ok().build() 
			: ResponseEntity.noContent().build();
	}

}