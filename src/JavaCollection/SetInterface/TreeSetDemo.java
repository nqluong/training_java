package JavaCollection.SetInterface;

import java.util.Comparator;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

public class TreeSetDemo {
    public static void main(String[] args) {

        NavigableSet<String> ts = new TreeSet<>();

        ts.add("Nguyen");
        ts.add("Quang");
        ts.add("Luong");
        ts.add("A");
        ts.add("B");
        ts.add("Z");

        System.out.println("Initial TreeSet " + ts);

        ts.remove("B");

        System.out.println("After removing element " + ts);

        ts.pollFirst();

        System.out.println("After removing first " + ts);

        ts.pollLast();

        System.out.println("After removing last " + ts);

        Set<StringBuffer> ts2 = new TreeSet<>(new Comparator<StringBuffer>() {
            @Override
            public int compare(StringBuffer sb1, StringBuffer sb2) {
                return sb1.toString().compareTo(sb2.toString());
            }
        });


        ts2.add(new StringBuffer("A"));
        ts2.add(new StringBuffer("Z"));
        ts2.add(new StringBuffer("L"));
        ts2.add(new StringBuffer("B"));
        ts2.add(new StringBuffer("O"));
        ts2.add(new StringBuffer("1"));

        // Printing the elements
        System.out.println(ts2);
    }
}
