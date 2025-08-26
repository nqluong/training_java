package JavaCollection.ListInterface;

import java.util.ArrayList;
import java.util.List;

public class ListInterfaceDemo {
    public static void main(String[] args) {
        List<String> li = new ArrayList<>();

        // Adding elements in List
        li.add("Java");
        li.add("Python");
        li.add("DSA");
        li.add("C++");

        System.out.println("Elements of List are:");

        // Iterating through the list
        for (String s : li) {
            System.out.println(s);
        }

        System.out.println("Element at Index 1: "+ li.get(1));


        li.set(1, "JavaScript");
        System.out.println("Updated List: " + li);

        li.remove("C++");
        System.out.println("List After Removing Element: " + li);


        List<String> al = new ArrayList<>();

        al.add("Nguyen");
        al.add("Luong");
        al.add(1, "Quang");

        System.out.println(al);
    }
}
