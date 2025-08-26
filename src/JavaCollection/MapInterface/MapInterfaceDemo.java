package JavaCollection.MapInterface;

import java.util.HashMap;
import java.util.Map;

public class MapInterfaceDemo {
    public static void main(String[] args) {
        Map<String, Integer> hm = new HashMap<String, Integer>();

        hm.put("a", Integer.valueOf(100));
        hm.put("b", Integer.valueOf(200));
        hm.put("c", Integer.valueOf(300));
        hm.put("d", Integer.valueOf(400));


        for (Map.Entry<String, Integer> me : hm.entrySet()) {

            System.out.print(me.getKey() + ":" + me.getValue() + "\n");
        }
        hm.put("c", Integer.valueOf(400));
        System.out.println("Updated Map: " + hm);
    }
}
