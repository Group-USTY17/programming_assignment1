package com.ru.usty.elevator;

public class Person implements Runnable {
	int currentFloor;
	int destinationFloor;
	ElevatorScene scene;
	
	@Override
	public void run() {
		//do something a person would do
		scene.personCount.set(currentFloor, scene.personCount.get(currentFloor) + 1); //add to visualized personcount
	}
	
	public Person(int source, int destination, ElevatorScene sourceClass) {
		currentFloor = source;
		destinationFloor = destination;
		scene = sourceClass;
	}
}
