package com.noname.todo.service;

import com.noname.todo.entity.Todo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TodoService {

    private final List<Todo> todos = new ArrayList<>();

    public List<Todo> getAll() {
        return todos;
    }

    public Optional<Todo> getById(final int id) {
        return todos.stream()
                .filter(todo -> todo.getId() == id)
                .findFirst();
    }

    public void add(final Todo todo) {
        todos.add(todo);
    }

    public boolean deleteById(final int id) {
        return todos.removeIf(todo -> todo.getId() == id);
    }

}
