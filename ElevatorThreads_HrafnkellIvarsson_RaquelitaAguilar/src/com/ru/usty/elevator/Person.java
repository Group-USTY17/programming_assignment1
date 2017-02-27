package com.ru.usty.elevator;

public class Person implements Runnable {
	private volatile boolean cancelled;
	private int currentFloor;
	private int destinationFloor;
	private Elevator currentElevator;
	private ElevatorScene scene;
	
	public static final int PERSON_CYCLE_TIME = 100; //thinking cycle of a person thread
	
	@Override
	public void run() {
		//do something a person would do
		while(!cancelled) {
			try {
				java.lang.Thread.sleep(PERSON_CYCLE_TIME); //wait one cycle
				
				if(currentElevator != null) { //if currently in an elevator
					currentFloor = currentElevator.getFloor(); //update current floor
					if(currentFloor == destinationFloor) {
						exitElevator();
					}
				}
				else { //currently not in an elevator
					Elevator availableElevator =  scene.floors[currentFloor].getAvailableElevator(); 
					if(availableElevator != null) {
						enterElevator(availableElevator);
					}
				}
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	//CONSTRUCTOR
	public Person(int source, int destination, ElevatorScene es) {
		currentFloor = source;
		destinationFloor = destination;
		scene = es;
	}
	
	//get persons destination
	public int getDestination() {
		return destinationFloor;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	//===================PRIVATE FUNCTIONS====================
	
	//adds the person to an elevator
	private void enterElevator(Elevator e) {
		if(e.addOccupant(this)) {
			currentElevator = e;
			scene.floors[currentFloor].removePerson(this);
		}
	}
	
	//removes the person from the elevator and kills the thread
	private void exitElevator() {	
		if(currentElevator.removeOccupant(this)) {
			currentElevator = null;
			exit(currentFloor);			
			cancel();
		}		
	}
	
	//report person exiting to elevatorscene
	private void exit(int floor) {
		scene.personExitsAtFloor(floor);
	}
	
	private void cancel() {
		cancelled = true;
	}
}
