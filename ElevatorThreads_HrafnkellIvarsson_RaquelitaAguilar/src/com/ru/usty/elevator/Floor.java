package com.ru.usty.elevator;
import java.util.ArrayList;

public class Floor {
	private int personCount;
	private ArrayList<Person> persons;
	
	public Floor() {
		personCount = 0;
		persons = new ArrayList<Person>();
	}
	
	public void addPerson(Person pers) {
		persons.add(pers);
		personCount++;
	}
	
	public int getPersonCount() {
		return personCount;
	}
	
	public ArrayList<Person> getPersons(int count) {
		if(count < 0) return new ArrayList<Person>();
		
		ArrayList<Person> personsLeavingFloor = new ArrayList<Person>();
		for(int i = 0; i < count; i++) {
			if(persons.isEmpty()) break;
			personsLeavingFloor.add(persons.get(0));
			personCount--;
			persons.remove(0);
		}
		
		return personsLeavingFloor;
	}

}
