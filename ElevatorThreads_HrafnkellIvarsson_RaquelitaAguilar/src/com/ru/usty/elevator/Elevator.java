package com.ru.usty.elevator;

public class Elevator implements Runnable {
	int currentFloor;
	int occupants;
	
	@Override
	public void run() {
		//do something an elevator would do
		
		//TEST CYCLING BETWEEN FLOORS
		for(int i = 0; i < 10000; i++) {
			try { java.lang.Thread.sleep(1000); }
			catch (InterruptedException e) { e.printStackTrace(); }
			currentFloor = i % 2;
		}
	}
	
	public Elevator(int floor) {
		currentFloor = floor;
		occupants = 0;
	}
	
	public int getFloor() {
		return currentFloor;
	}
	
	public int getOccupants() {
		return occupants;
	}
}
