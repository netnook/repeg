package net.netnook.qpeg.examples.todo.model;

import java.util.ArrayList;
import java.util.List;

public class Project {

	private String name;
	private List<Task> tasks = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public Task getTask(int idx) {
		return tasks.get(idx);
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public void addTask(Task task) {
		this.tasks.add(task);
	}
}
