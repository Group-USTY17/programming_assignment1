package com.ru.usty.elevator;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Floor {
	private int personCount;
	private ArrayList<Elevator> elevators;
	private static Semaphore personCountMutex;
	private static Semaphore elevatorListMutex;
	
	//CONSTRUCTOR
	public Floor() {
		personCount = 0;
		elevators = new ArrayList<Elevator>();
		personCountMutex = new Semaphore(1);
		elevatorListMutex = new Semaphore(1);
	}
	
	//adds a person
	public void addPerson() {
		try {
			personCountMutex.acquire();
			personCount++;
			personCountMutex.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
	}
	
	//removes a person
	public void removePerson() {
		try {
			personCountMutex.acquire();
			personCount--;
			personCountMutex.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	//add elevator to elevator list
	public void elevatorArrives(Elevator elev) {
		try {
			elevatorListMutex.acquire();
			elevators.add(elev);
			elevatorListMutex.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	//remove elevator from elevator list
	public void elevatorLeaves(Elevator elev) {
		try {
			elevatorListMutex.acquire();
			elevators.remove(elev);
			elevatorListMutex.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	//returns an available elevator
	public Elevator getAvailableElevator() {
		for(int i = 0; i < elevators.size(); i++) {
			if(!elevators.get(i).atCapacity()) return elevators.get(i);
		}
		
		return null;
	}
 	
	//returns number of persons waiting at floor
	public int getPersonCount() {
		return personCount;
	}

}
