package JavaCollection.MapInterface;

import java.util.LinkedHashMap;
import java.util.Map;

public class LinkedHashMapDemo {
    public static void main(String[] args) {

        LinkedHashMap<Integer, String> lhm = new LinkedHashMap<Integer, String>();

        lhm.put(3, "Nguyen");
        lhm.put(2, "Quang");
        lhm.put(1, "Luong");

        System.out.println("" + lhm);

        lhm.remove(2);
        System.out.println("After removing element " + lhm);

        for (Map.Entry<Integer, String> mapElement :
                lhm.entrySet()) {
            Integer k = mapElement.getKey();

            String v = mapElement.getValue();

            // Printing the key-value pairs
            System.out.println(k + " : " + v);
        }
    }
}
