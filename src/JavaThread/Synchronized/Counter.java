package JavaThread.Synchronized;

public class Counter {
    private int count;

    public synchronized void increment() {
        count++;
        System.out.println(Thread.currentThread().getName() + ": " + count);
    }

    public int getCount() {
        return count;
    }
}
