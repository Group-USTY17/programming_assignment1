package com.ru.usty.elevator;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
 * The base function definitions of this class must stay the same
 * for the test suite and graphics to use.
 * You can add functions and/or change the functionality
 * of the operations at will.
 *
 */

public class ElevatorScene {

	//TO SPEED THINGS UP WHEN TESTING,
	//feel free to change this.  It will be changed during grading
	public static final int VISUALIZATION_WAIT_TIME = 250;  //milliseconds

	private int numberOfFloors;
	private int numberOfElevators;
	private int activePersons;

	ArrayList<Integer> exitedCount = null;
	public static Semaphore exitedCountMutex;
	
	Elevator[] elevators;
	Thread[] elevatorThreads;
	Floor[] floors;

	//Base function: definition must not change
	//Necessary to add your code in this one
	public void restartScene(int numberOfFloors, int numberOfElevators) {	
		cleanUpScene();
		initializeScene(numberOfFloors, numberOfElevators);
	}

	//Base function: definition must not change
	//Necessary to add your code in this one
	public Thread addPerson(int sourceFloor, int destinationFloor) {
		activePersons++;
		floors[sourceFloor].addPerson();
		Thread newPerson = new Thread(new Person(sourceFloor, destinationFloor, this));
		newPerson.start();
		return newPerson;
	}

	//Base function: definition must not change, but add your code
	public int getCurrentFloorForElevator(int elevator) {
		if(elevators == null) return -1; //in case the function is called before initializing elevators
		try { 
			int floor = elevators[elevator].getFloor();
			return floor;
		}
		catch (java.lang.NullPointerException e) {
			return -1;
		}
	}

	//Base function: definition must not change, but add your code
	public int getNumberOfPeopleInElevator(int elevator) {
		if(elevators == null) return -1; //in case the function is called before initializing elevators
		try {
			int occupantCount = elevators[elevator].getOccupantCount();
			return occupantCount;
		}
		catch (java.lang.NullPointerException e) {
			return -1;
		}
	}

	//Base function: definition must not change, but add your code
	public int getNumberOfPeopleWaitingAtFloor(int floor) {
		if(floors == null) return 0; //in case the function is called before initializing floor
		return floors[floor].getPersonCount();
	}

	//Base function: definition must not change, but add your code if needed
	public int getNumberOfFloors() {
		return numberOfFloors;
	}

	//Base function: definition must not change, but add your code if needed
	public void setNumberOfFloors(int numberOfFloors) {
		this.numberOfFloors = numberOfFloors;
	}

	//Base function: definition must not change, but add your code if needed
	public int getNumberOfElevators() {
		return numberOfElevators;
	}

	//Base function: definition must not change, but add your code if needed
	public void setNumberOfElevators(int numberOfElevators) {
		this.numberOfElevators = numberOfElevators;
	}

	//Base function: no need to change unless you choose
	//				 not to "open the doors" sometimes
	//				 even though there are people there
	public boolean isElevatorOpen(int elevator) {

		return isButtonPushedAtFloor(getCurrentFloorForElevator(elevator));
	}
	//Base function: no need to change, just for visualization
	//Feel free to use it though, if it helps
	public boolean isButtonPushedAtFloor(int floor) {

		return (getNumberOfPeopleWaitingAtFloor(floor) > 0);
	}

	//Person threads must call this function to
	//let the system know that they have exited.
	//Person calls it after being let off elevator
	//but before it finishes its run.
	public void personExitsAtFloor(int floor) {
		try {
			
			exitedCountMutex.acquire();
			exitedCount.set(floor, (exitedCount.get(floor) + 1));
			activePersons--;
			exitedCountMutex.release();

		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	//Base function: no need to change, just for visualization
	//Feel free to use it though, if it helps
	public int getExitedCountAtFloor(int floor) {
		if(exitedCount.size() == 0) return 0; //prevents errors when resetting between tests
		
		if(floor < getNumberOfFloors()) {
			return exitedCount.get(floor);
		}
		else {
			return 0;
		}
	}
	
	public int getActivePersonCount() {
		return activePersons;
	}
	
	//===================PRIVATE FUNCTIONS====================

	private void cleanUpScene() {
		activePersons = 0; //reset active persons
		
		//cancel and join() elevator threads.
		if(elevators != null) { 
			for(int i = 0; i < numberOfElevators; i++) {
				elevators[i].cancel();
				try {
					elevatorThreads[i].join();
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		elevators = null;
		
		//set floors to null
		floors = null;
		
		//reset exited count
		if(exitedCount != null) {
			exitedCount.clear();
		}	
	}
	
	private void initializeScene(int numberOfFloors, int numberOfElevators) {
		//set number of floors and number of elevators class variables
		this.numberOfFloors = numberOfFloors;
		this.numberOfElevators = numberOfElevators;
		
		//initialize floors
		floors = new Floor[numberOfFloors];
		for(int i = 0; i < numberOfFloors; i++) {
			floors[i] = new Floor();
		}
		
		//initialize elevators
		elevators = new Elevator[numberOfElevators];
		elevatorThreads = new Thread[numberOfElevators];
		for(int i = 0; i < numberOfElevators; i++) {
			elevators[i] = new Elevator(0, numberOfFloors, this); //initialize all elevators at floor 0
			elevatorThreads[i] = new Thread(elevators[i]);
			elevatorThreads[i].start();
		}
		
		//initialize exited count
		if(exitedCount == null) {
			exitedCount = new ArrayList<Integer>();
		}
		for(int i = 0; i < numberOfFloors; i++) {
			this.exitedCount.add(0);
		}
		exitedCountMutex = new Semaphore(1);
		
	}


}
