package JavaCollection.SetInterface;

import java.util.HashSet;
import java.util.Iterator;

public class HashSetDemo {
    public static void main(String[] args) {
        HashSet<String> hs = new HashSet<String>();

        hs.add("Nguyen");
        hs.add("Quang");
        hs.add("Luong");
        hs.add("A");
        hs.add("B");
        hs.add("Z");

        System.out.println("HashSet : " + hs);

        // Removing the element B
        hs.remove("B");

        // Printing the updated HashSet elements
        System.out.println("HashSet after removing element : " + hs);

        // Returns false if the element is not present
        System.out.println("B exists in Set : " + hs.remove("B"));

        System.out.print("Using iterator : ");
        Iterator<String> iterator = hs.iterator();

        while (iterator.hasNext())
            System.out.print(iterator.next() + ", ");

        System.out.println();

        System.out.print("Using enhanced for loop : ");
        for (String element : hs)
            System.out.print(element + " , ");
    }
}
