package com.noname.todo.controller;

import com.noname.todo.entity.Todo;
import com.noname.todo.service.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public ResponseEntity<List<Todo>> getAll() {
        final var todos = todoService.getAll();
        return todos.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(todos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todo> getById(@PathVariable int id) {
        Optional<Todo> optTodo = todoService.getById(id);

        return optTodo.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Void> addTodo(@RequestBody Todo todo) {
        todoService.add(todo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable int id) {
        var isDeleted = todoService.deleteById(id);
        return isDeleted
                ? ResponseEntity.ok().build()
                : ResponseEntity.noContent().build();
    }

}