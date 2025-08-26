package JavaCoreBasic.HandleException;

import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        ExceptionDemo demo = new ExceptionDemo();

        System.out.println("Try - catch demo(unchecked exception):");
        try {
            demo.divide(10, 0);
        }
        catch (ArithmeticException e) {
            System.out.println("Caught an exception: " + e.getMessage());
        }

        System.out.println("Try - catch demo(checked exception):");

        try {
            demo.checkAge(14);
        } catch (MyCheckedException e) {
            System.out.println("Caught an exception: " + e.getMessage());
        }

        System.out.println("Demo try with resources:");
        try (FileReader fileReader = new FileReader("nonexistentfile.txt")) {
           int countData = 0;
           while((countData = fileReader.read()) != -1){
               System.out.println((char) countData);
           }
        } catch (IOException e) {
            System.out.println("Caught an exception: " + e.getMessage());

        }
    }
}
