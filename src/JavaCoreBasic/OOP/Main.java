package JavaCoreBasic.OOP;

import JavaCoreBasic.OOP.vehicles.Car;
import JavaCoreBasic.OOP.vehicles.Motorcycle;
import JavaCoreBasic.OOP.vehicles.Vehicle;

public class Main {
    public static void main(String[] args) {
        Car myCar = new Car("Honda CX", 200, 25000.0, 4);

        System.out.println("--- Car ---");
        myCar.displayCarDetails();
        myCar.start();
        myCar.stop();
        System.out.println("Final cost of myCar: $ " + myCar.calculateCost());
        System.out.println();

        Motorcycle myMotorcycle = new Motorcycle("Yamaha", 250.0, 10000.0, false);

        System.out.println("--- Motorcycle ---");
        myMotorcycle.displayMotorcycleDetails();
        myMotorcycle.start();
        myMotorcycle.stop();
        System.out.println("Final cost of myMotorcycle: $" + myMotorcycle.calculateCost());
        System.out.println();

        System.out.println("--- Polymorphism ---");
        Vehicle polyVehicle1 = new Car("Toyota Camry", 200.0, 30000.0, 4);
        Vehicle polyVehicle2 = new Motorcycle("Wave", 100.0, 15000.0, true);

        System.out.println("Cost of polyVehicle1 (Car): $" + polyVehicle1.calculateCost());
        System.out.println("Cost of polyVehicle2 (Motorcycle): $" + polyVehicle2.calculateCost());
    }
}
