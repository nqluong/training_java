package JavaCoreBasic.String;

public class Main {
    public static void main(String[] args) {
        System.out.println("1. Dac diem & tinh chat");
        String str1 = "nguyen";
        System.out.println("str1: " + str1);
        str1.concat("luong");
        System.out.println("str1 (sau concat): " + str1);

        String str2 = str1.concat("luong");
        System.out.println("str2: " + str2);

        System.out.println("2. Cac cach tao string");
        String s1 = "Hello";                    //literal
        String s2 = new String("Hello");        //new
        char[] chars = {'H', 'e', 'l', 'l', 'o'};
        String s3 = new String(chars);      //tu char[]
        byte[] bytes = {72, 101, 108, 108, 111};
        String s4 = new String(bytes);          //tu byte
        String s5 = String.valueOf(100);        //valueOf
        String s6 = String.format("Hello %s!", "Java");     //format

        System.out.println("Literal: " + s1);
        System.out.println("new String: " + s2);
        System.out.println("From char[]: " + s3);
        System.out.println("From byte[]: " + s4);
        System.out.println("valueOf: " + s5);
        System.out.println("format: " + s6);


        System.out.println("3. String pool");
        String pool1 = "Pool1";
        String pool2 = "Pool1";
        String pool3 = new String("Pool1");

        System.out.println("pool1 == pool2 :" + (pool1 == pool2));      //true
        System.out.println("pool1 == pool3 :" + (pool1 == pool3));      //false
        System.out.println("pool2.equals(pool3) :" + pool2.equals(pool3)); // true

        String pool4 = pool3.intern();
        System.out.println("pool1 == pool4:" + (pool1 == pool4)); //true

        System.out.println("4. So sanh chuoi");
        String cmp1 = "Java";
        String cmp2 = "Java";
        String cmp3 = new String("Java");
        String cmp4 ="Java";

        System.out.println("cmp1 == cmp2 :" + (cmp1 == cmp2));             // true
        System.out.println("cmp1 == cmp3 :" + (cmp1 == cmp3));             // false
        System.out.println("cmp1.equals(cmp3) :" + cmp1.equals(cmp3));     // true
        System.out.println("cmp1.equalsIgnoreCase(cmp4) :" + cmp1.equalsIgnoreCase(cmp4)); // true
        System.out.println("cmp1.compareTo(cmp4) :" + cmp1.compareTo(cmp4)); //  0 (so sánh từ điển)

    }
}
