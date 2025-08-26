package JavaThread.Synchronized;

public class CounterStatic {
    private static int count;

    public static synchronized void increment() {
        count++;
        System.out.println(Thread.currentThread().getName() + ": " + count);
    }
}
