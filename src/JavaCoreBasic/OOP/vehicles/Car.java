package JavaCoreBasic.OOP.vehicles;

import JavaCoreBasic.OOP.interfaces.Movable;

public class Car extends Vehicle implements Movable {

    private int numberOfDoors;

    public Car(String model, double speed, double price, int numberOfDoors) {
        super(model, speed, price);
        this.numberOfDoors = numberOfDoors;
    }

    @Override
    public double calculateCost() {
        return this.getPrice() + 2000;
    }

    @Override
    public void start() {
        System.out.println("The car is starting");
    }

    public void displayCarDetails() {
        this.displayInfo();
        System.out.println("Number of doors: " + this.numberOfDoors);
        System.out.println("Final cost : $" + this.calculateCost());
    }
}
