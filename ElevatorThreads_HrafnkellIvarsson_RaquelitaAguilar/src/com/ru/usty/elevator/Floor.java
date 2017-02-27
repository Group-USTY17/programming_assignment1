package com.ru.usty.elevator;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Floor {
	private int personCount;
	private ArrayList<Elevator> elevators;
	private static Semaphore personCountMutex;
	private static Semaphore elevatorListMutex;
	
	public Floor() {
		personCount = 0;
		elevators = new ArrayList<Elevator>();
		personCountMutex = new Semaphore(1);
		elevatorListMutex = new Semaphore(1);
	}
	
	public void addPerson(Person pers) {
		try {
			personCountMutex.acquire();
			personCount++;
			personCountMutex.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public void removePerson(Person pers) {
		try {
			personCountMutex.acquire();
			personCount--;
			personCountMutex.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void elevatorArrives(Elevator elev) {
		try {
			elevatorListMutex.acquire();
			elevators.add(elev);
			elevatorListMutex.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void elevatorLeaves(Elevator elev) {
		try {
			elevatorListMutex.acquire();
			elevators.remove(elev);
			elevatorListMutex.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Elevator getAvailableElevator() {
		for(int i = 0; i < elevators.size(); i++) {
			if(!elevators.get(i).atCapacity()) return elevators.get(i);
		}
		
		return null;
	}
 	
	public int getPersonCount() {
		return personCount;
	}

}
