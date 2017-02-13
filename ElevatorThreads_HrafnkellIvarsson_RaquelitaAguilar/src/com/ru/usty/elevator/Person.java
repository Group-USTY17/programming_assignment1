package com.ru.usty.elevator;

public class Person implements Runnable {
	private int currentFloor;
	private int destinationFloor;
	private ElevatorScene scene;
	
	@Override
	public void run() {
		//do something a person would do
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
		scene.personExitsAtFloor(currentFloor);
	}
}
