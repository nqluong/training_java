package JavaCoreBasic.Memory;

public class Main {
    public static void main(String[] args) {
        //cap phat tinh
        int x = 100;
        int y = 10;

        //Cap phat dong
        Person person1 = new Person("Person 1");
        Person person2 = new Person("Person 2");

        System.out.println("x = " + x);
        System.out.println("y = " + y);
        System.out.println("person1 = " + person1.getName());
        System.out.println("person2 = " + person2.getName());
    }
}
