package JavaCollection.SetInterface;

import java.util.Iterator;
import java.util.LinkedHashSet;

public class LinkedHashSetDemo {
    public static void main(String[] args) {
        LinkedHashSet<String> lh = new LinkedHashSet<String>();

        lh.add("Nguyen");
        lh.add("Quang");
        lh.add("Luong");
        lh.add("A");
        lh.add("B");
        lh.add("Z");
        lh.add("A");

        System.out.println("" + lh);

        lh.remove("B");

        System.out.println("After removing element " + lh);

        System.out.println(lh.remove("AC"));

        Iterator itr = lh.iterator();

        while (itr.hasNext())
            System.out.print(itr.next() + ", ");

        System.out.println();

        for (String s : lh)
            System.out.print(s + ", ");
        System.out.println();
    }
}
