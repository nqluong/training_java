package JavaCollection.ListInterface;

import java.util.Vector;

public class VectorDemo {

    public static void main(String[] args) {
        Vector<Integer> v = new Vector<>(3, 2);

        v.addElement(1);
        v.addElement(2);
        v.addElement(3);

        v.insertElementAt(0, 1);

        v.removeElementAt(2);

        for (int i : v) {
            System.out.println(i);
        }

        Vector<Integer> vec = new Vector<>();

       System.out.println("Default Capacity: " + vec.capacity()); // Output: 10


        vec.add(12);
        vec.add(23);
        vec.add(22);
        vec.add(10);
        vec.add(20);

        System.out.println("Vector: " + vec);

        // Using set() method to replace 12 with 21
        System.out.println("The Object that is replaced is: "
                + vec.set(0, 21));

        // Using set() method to replace 20 with 50
        System.out.println("The Object that is replaced is: "
                + vec.set(4, 50));

        System.out.println("The new Vector is:" + vec);
    }
}
