package JavaCollection.SetInterface;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class SetInterfaceDemo {
    public static void main(String[] args) {
        Set<String> h = new HashSet<String>();

        h.add("A");
        h.add("B");
        h.add("C");
        h.add("B");
        h.add("D");
        h.add("E");


        for (String value : h)
            System.out.print(value + ", ");

        System.out.println();


    }
}

