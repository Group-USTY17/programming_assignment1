package com.ru.usty.elevator;

public class Elevator implements Runnable {
	int currentFloor;
	int occupants;
	int floorCount;
	
	@Override
	public void run() {
		//do something an elevator would do
		
		//TEST CYCLING BETWEEN FLOORS
		for(int i = 0; i < 10000; i++) {
			try { java.lang.Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME); }
			catch (InterruptedException e) { e.printStackTrace(); }
			currentFloor = i % floorCount;
		}
	}
	
	public Elevator(int floor, int floors) {
		currentFloor = floor;
		floorCount = floors;
		occupants = 0;
	}
	
	public int getFloor() {
		return currentFloor;
	}
	
	public int getOccupants() {
		return occupants;
	}
}
