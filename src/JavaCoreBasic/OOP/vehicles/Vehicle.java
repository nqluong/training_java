package JavaCoreBasic.OOP.vehicles;

public abstract class Vehicle {
    private String model;
    private double speed;
    private double price;

    public Vehicle(String model, double speed, double price) {
        this.model = model;
        this.speed = speed;
        this.price = price;
    }

    public abstract double calculateCost();

    public final void displayInfo(){
        System.out.println("Model: " + this.model);
        System.out.println("Speed: " + this.speed + "km/h");
        System.out.println("Base Price: $" + this.price);
    }

    public void displayInfo(String type){
        System.out.println("Type: " + type);
        System.out.println("Model: " + this.model);
        System.out.println("Speed: " + this.speed + "km/h");
        System.out.println("Base Price: $" + this.price);
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
