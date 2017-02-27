package com.ru.usty.elevator;

public class Person implements Runnable {
	private volatile boolean cancelled;
	private int currentFloor;
	private int destinationFloor;
	private Elevator currentElevator;
	private ElevatorScene scene;
	
	@Override
	public void run() {
		//do something a person would do
		while(!cancelled) {
			try { java.lang.Thread.sleep(100); }
			catch (InterruptedException e) { e.printStackTrace(); }
			
			if(currentElevator != null) {
				currentFloor = currentElevator.getFloor();
				
				if(currentFloor == destinationFloor) {
					exitElevator();
				}
			}
			else {
				Elevator availableElevator =  scene.floors[currentFloor].getAvailableElevator();
				if(availableElevator != null) {
					enterElevator(availableElevator);
				}
			}
			
			//debugPerson();
		}
	}
	
	public Person(int source, int destination, ElevatorScene es) {
		currentFloor = source;
		destinationFloor = destination;
		scene = es;
	}
	
	public int getDestination() {
		return destinationFloor;
	}
	
	public boolean isDestination(int destination) {
		return destinationFloor == destination;
	}
	
	public void exit() {
		scene.personExitsAtFloor(destinationFloor);
	}
	
	public void cancel() {
		cancelled = true;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	//PRIVATE FUNCTIONS
	
	private void enterElevator(Elevator e) {
		if(e.addOccupant(this)) {
			currentElevator = e;
			scene.floors[currentFloor].removePerson(this);
		}
	}
	
	private void exitElevator() {	
		if(currentElevator.removeOccupant(this)) {
			currentElevator = null;
			exit();			
			cancel();
		}
			
	}
	
	private void debugPerson() {
		System.out.println("Current Floor: " + currentFloor);
		System.out.println("Destination Floor: " + destinationFloor);
		if(currentElevator != null) System.out.println("Currently In Elevator");
		else System.out.println("Currently NOT In Elevator");
		System.out.println();
	}
}
