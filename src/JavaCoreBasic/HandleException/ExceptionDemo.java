package JavaCoreBasic.HandleException;

import java.io.FileReader;
import java.io.IOException;

public class ExceptionDemo {

    public void readFile(String filePath) throws IOException {
        FileReader fileReader = new FileReader(filePath);

        fileReader.close();
    }

    public void checkAge(int age) throws MyCheckedException {
        if (age < 18) {
            throw new MyCheckedException("Age must be at least 18");
        }

        System.out.println("Age is valid: " + age);
    }

    public void divide(int a, int b){
        if(b == 0){
            throw new ArithmeticException("Division by zero is not allowed.");
        } else {
            int result = a / b;
            System.out.println("Result: " + result);
        }
    }

}
