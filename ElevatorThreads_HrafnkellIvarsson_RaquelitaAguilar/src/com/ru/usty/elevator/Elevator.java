package com.ru.usty.elevator;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Elevator implements Runnable {
	private volatile boolean cancelled;
	private int currentFloor;
	private int floorCount;
	public static final int MAX_CAPACITY = 6;
	private int direction; //0 == down, 1 == up
	private ArrayList<Person> occupants;
	private ElevatorScene scene;
	private static Semaphore occupantsMutex;
	
	@Override
	public void run() {
		scene.floors[currentFloor].elevatorArrives(this); //place elevator at first floor
		while (!cancelled) {
			try {
				java.lang.Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME); //wait for persons
				//now do stuff
				changeFloor();
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	//CONSTRUCTOR
	public Elevator(int floor, int floors, ElevatorScene es) {
		currentFloor = floor;
		floorCount = floors;
		//capacity = 6; //this variable might need some fine tuning
		scene = es;
		occupants = new ArrayList<Person>();
		direction = 1;
		occupantsMutex = new Semaphore(1);
	}
	
	//returns the current floor of the elevator
	public int getFloor() {
		return currentFloor;
	}
	
	//returns the number of people in the elevator
	public int getOccupantCount() {
		return occupants.size();
	}
	
	//adds a person to the elevator
	public boolean addOccupant(Person pers) {
		try {
			occupantsMutex.acquire();
			if(atCapacity()) { //if the elevator is full
				occupantsMutex.release();
				return false;
			}
			
			occupants.add(pers);
			occupantsMutex.release();
			return true;
		} catch (InterruptedException e) {
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
			e.printStackTrace();
		}
		
		return false;
	}
	
	//is the elevator full?
	public boolean atCapacity() {
		return occupants.size() >= MAX_CAPACITY;
	}
	
	public void cancel() {
		cancelled = true;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	//===================PRIVATE FUNCTIONS====================
	
	//calculate what floor the elevator should go to next
	private void changeFloor() {
		//TODO make an algorithm that is'nt stupid
		if(scene.getActivePersonCount() == 0) return; //no active persons, do nothing
		
		if(occupants.size() <= 0){ //is the elevator empty?
			goNext();
		}
		else {
			if(occupants.get(0).getDestination() > currentFloor) goUp();
			else goDown();
		}
	}
	
	//move elevator up
	private boolean goUp() {
		if (currentFloor >= floorCount) return false;
		else {
			scene.floors[currentFloor].elevatorLeaves(this);
			currentFloor++;
			direction = 1;
			scene.floors[currentFloor].elevatorArrives(this);
			return true;
		}
	}
	
	//move elevator down
	private boolean goDown() {
		if (currentFloor <= 0) return false;
		else {
			scene.floors[currentFloor].elevatorLeaves(this);
			currentFloor--;
			direction = 0;
			scene.floors[currentFloor].elevatorArrives(this);
			return true;
		}
	}
	
	//go up or down depending on current elevator direction
	private void goNext() {
		if (currentFloor == floorCount-1) direction = 0; //at top, go down
		else if (currentFloor == 0) direction = 1; //at bottom, go up
		
		if (direction == 1) goUp();
		else goDown();
	}
	
}
