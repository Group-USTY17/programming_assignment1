package com.ru.usty.elevator;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Elevator implements Runnable {
	private volatile boolean cancelled;
	private int currentFloor;
	private int floorCount;
	private int capacity;
	private int direction;
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
	
	public Elevator(int floor, int floors, ElevatorScene es) {
		currentFloor = floor;
		floorCount = floors;
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
