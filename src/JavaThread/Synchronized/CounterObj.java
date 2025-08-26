package JavaThread.Synchronized;

public class CounterObj {
    private int count;

    public synchronized void increment() {
        count++;
        System.out.println(Thread.currentThread().getName() +" - Count: " + count);
    }

    public int getCount() {
        return count;
    }
}
