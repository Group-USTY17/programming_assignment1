package com.ru.usty.elevator;
import java.util.ArrayList;

public class Floor {
	private int personCount;
	private ArrayList<Person> persons;
	private ArrayList<Elevator> elevators;
	
	public Floor() {
		personCount = 0;
		persons = new ArrayList<Person>();
		elevators = new ArrayList<Elevator>();
	}
	
	public void addPerson(Person pers) {
		//persons.add(pers);
		personCount++;
	}
	
	public void removePerson(Person pers) {
		//persons.remove(pers);
		personCount--;
	}
	
	public void elevatorArrives(Elevator e) {
		elevators.add(e);
	}
	
	public void elevatorLeaves(Elevator e) {
		elevators.remove(e);
	}
	
	public Elevator getAvailableElevator() {
		if(elevators.size() > 0) {
			return elevators.get(0);
		}
		
		return null;
	}
 	
	public int getPersonCount() {
		return personCount;
	}
	
	//TODO TO BE DEPRECATED
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
