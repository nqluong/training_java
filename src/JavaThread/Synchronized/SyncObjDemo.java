package JavaThread.Synchronized;

public class SyncObjDemo {
    public static void main(String[] args) {
        CounterObj counter1 = new CounterObj();
        CounterObj counter2 = new CounterObj();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                counter1.increment();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "thread1");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                counter2.increment();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "thread2");

        t1.start();
        t2.start();
    }
}
