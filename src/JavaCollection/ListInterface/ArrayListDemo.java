package JavaCollection.ListInterface;

import java.util.ArrayList;

public class ArrayListDemo {
    public static void main(String[] args) {
        ArrayList<String> al = new ArrayList<>();

        al.add("Nguyen");
        al.add("Luong");

        System.out.println("Original List : "+al);

        // Adding Elements at the specific index
        al.add(1, "Quang");

        System.out.println("After Adding element at index 1 : "+ al);

        al.remove(0);
        System.out.println("Element removed from index 0 : "+ al);


        al.remove("Quang");

        System.out.println("Element Geeks removed : "+ al);


        al.set(0, "NQL");

        System.out.println("List after updation of value : "+al);
    }
}
