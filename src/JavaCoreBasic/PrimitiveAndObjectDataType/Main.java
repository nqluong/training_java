package JavaCoreBasic.PrimitiveAndObjectDataType;

public class Main {
    public static void main(String[] args) {
        PrimitiveAndObjectDataType demo = new PrimitiveAndObjectDataType();

        int a = 10;
        Integer b = 10;
        String c = "NguyenLuong";

        System.out.println("1.");
        System.out.println("a = " + a);
        System.out.println("b = " + b);
        System.out.println("c = " + c);

        System.out.println("2.");
        int d = 20;
        Integer obj = Integer.valueOf(d); // primitive -> object
        int e = obj.intValue(); // object -> int (unboxing)
        System.out.println("Primitive -> Object: "  + obj);
        System.out.println("Object -> Primitive: " + e);

        int x = 30;
        Integer y = 30;
        Integer z = new Integer(30);

        System.out.println("3.");
        System.out.println("x == y:" + (x == y)); // true (unbox)
        System.out.println("y == z:" + (y == z)); // false (so sanh tham chieu)
        System.out.println("y == z:" + y.equals(z)); // true (so sanh gia tri)

        System.out.println("4.");
        System.out.println("Primitive Int:" + demo.primitiveInt);
        System.out.println("Object Int:" + demo.objectInt);
        System.out.println("Primitive Boolean:" + demo.primitiveBoolean);
        System.out.println("Object String:" + demo.objectString);
    }
}
