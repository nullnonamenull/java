package com.noname.todo.entity;

public class Todo {
	
	private int id;
	
	private String title;
	
	private boolean completed;

	public Todo() {}

	public Todo(int id, String title, boolean completed) {
		this.id = id;
		this.title = title;
		this.completed = completed;
	}

	public int getId() {
		return this.id;
	}

	public String getTitle() {
		return this.title;
	}

	public boolean getCompleted() {
		return completed;
	}

}