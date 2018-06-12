package net.netnook.repeg.examples.persons;

import java.util.List;

public class Persons {

	private List<Person> persons;

	public List<Person> getPersons() {
		return persons;
	}

	public void setPersons(List<Person> persons) {
		this.persons = persons;
	}

	public Person get(int i) {
		return persons.get(i);
	}
}
