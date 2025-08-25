package JavaCoreBasic.OOP.vehicles;

import JavaCoreBasic.OOP.interfaces.Movable;

public class Motorcycle extends Vehicle implements Movable {

    private boolean hasSidecar;

    public Motorcycle(String model, double speed, double price, boolean hasSidecar) {
        super(model, speed, price);
        this.hasSidecar = hasSidecar;
    }
    @Override
    public void start() {
        System.out.println("Motorcycle started");
    }

    @Override
    public double calculateCost() {
        return this.getPrice() + 1000;
    }

    public void displayMotorcycleDetails() {
        this.displayInfo();
        System.out.println("Has sidecar: " + this.hasSidecar);
        System.out.println("Final cost: $" + this.calculateCost());
    }
}
