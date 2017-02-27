package com.ru.usty.elevator;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Elevator implements Runnable {
	private volatile boolean cancelled;
	private int currentFloor;
	private int occupantCount;
	private int floorCount;
	private int capacity;
	private int direction;
	private ArrayList<Person> occupants;
	private ElevatorScene scene;
	private static Semaphore occupantsMutex;
	
	@Override
	public void run() {
		//do something an elevator would do
		scene.floors[currentFloor].elevatorArrives(this);
		while (!cancelled) {
			//wait for persons
			try { java.lang.Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME); }
			catch (InterruptedException e) { e.printStackTrace(); }
			
			//now do stuff
			//removeAllAtDestination();
			//fillToCapacity();
			changeFloor();
		}
	}
	
	public Elevator(int floor, int floors, ElevatorScene es) {
		currentFloor = floor;
		floorCount = floors;
		occupantCount = 0;
		capacity = 6; //this variable might need some fine tuning
		scene = es;
		occupants = new ArrayList<Person>();
		direction = 1;
		occupantsMutex = new Semaphore(1);
	}
	
	public int getFloor() {
		return currentFloor;
	}
	
	public int getOccupantCount() {
		return occupants.size();
	}
	
	public boolean addOccupant(Person pers) {
		try {
			occupantsMutex.acquire();
			if(occupants.size() >= capacity) {
				occupantsMutex.release();
				return false;
			}
			
			occupants.add(pers);
			occupantsMutex.release();
			return true;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean removeOccupant(Person pers) {
		try {
			occupantsMutex.acquire();
			occupants.remove(pers);
			occupantsMutex.release();
			return true;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean atCapacity() {
		return occupants.size() >= capacity;
	}
	
	public void cancel() {
		cancelled = true;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	//PRIVATE FUNCTIONS BELOW
	
	//remove the occupants that are at their destination floor
	//TODO TO BE DEPRECATED
	private void removeAllAtDestination() {
		if(occupantCount <= 0) return;
		
		//TODO maybe fix this unefficient method.
		while(removeAtDestination());
	}
	
	//TODO TO BE DEPRECATED
	private boolean removeAtDestination() {
		for(int i = 0; i < occupantCount; i++) {
			if(occupants.get(i).isDestination(currentFloor)) {
				removeOccupant(i);
				return true;
			}
		}
		return false;
	}
	
	//remove an occupant
	//TODO TO BE DEPRECATED
	private void removeOccupant(int at) {
		Person occupant = occupants.get(at);
		occupants.remove(at);
		occupant.exit();
		occupantCount--;
	}
	
	//calculate what floor the elevator should go to next
	private void changeFloor() {
		//TODO make an algorithm that is'nt stupid
		/*
		if(occupantCount <= 0){ //is the elevator empty?
			if(!goDown()) goUp(); //go down unless at bottom floot
		}
		else {
			if(occupants.get(0).getDestination() > currentFloor) goUp();
			else goDown();
		}
		*/
		
		scene.floors[currentFloor].elevatorLeaves(this);
		goNext(); //for simple testing
		scene.floors[currentFloor].elevatorArrives(this);
	}
	
	//move elevator up
	private boolean goUp() {
		if (currentFloor >= floorCount) return false;
		else {
			currentFloor++;
			return true;
		}
	}
	
	//move elevator down
	private boolean goDown() {
		if (currentFloor <= 0) return false;
		else {
			currentFloor--;
			return true;
		}
	}
	
	//testing function for just going up and down
	private void goNext() {
		if (currentFloor == floorCount-1) direction = 0;
		else if (currentFloor == 0) direction = 1;
		
		if (direction == 1) goUp();
		else goDown();
	}
	
}
