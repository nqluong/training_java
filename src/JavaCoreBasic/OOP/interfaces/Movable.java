package JavaCoreBasic.OOP.interfaces;

public interface Movable {

    void start();

    default void stop() {
        System.out.println("The vehicle has stopped.");
    }
}
