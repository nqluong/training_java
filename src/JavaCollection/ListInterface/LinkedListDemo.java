package JavaCollection.ListInterface;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class LinkedListDemo {
    public static void main(String[] args) {
        LinkedList<String> l = new LinkedList<>();

        l.add("One");
        l.add("Two");
        l.add("Three");
        l.add("Four");
        l.add("Five");

        System.out.println(l);

        l.add(2, "Six");
        System.out.println("After adding element at index 2: " + l);

        l.remove("Two");
        System.out.println("After removing element 'Two': " + l);

        ListIterator<String> it = l.listIterator();
        while (it.hasNext()) {
            String s = it.next();
            if (s.equals("Six")) {
                it.remove();
                it.add("Six new");
            }
        }
        System.out.println("After modifying elements using ListIterator: " + l);
    }
}
