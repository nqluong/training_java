package JavaThread.Synchronized;

public class SyncStaticDemo {
    public static void main(String[] args) {
        Thread t1 = new Thread(()->{
            for(int i = 0; i < 5; i++){
                CounterStatic.increment();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "Thread-1");

        Thread t2 = new Thread(()->{
            for(int i = 0; i < 5; i++){
                CounterStatic.increment();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "Thread-2");

        Thread t3 = new Thread(()->{
            for(int i = 0; i < 5; i++){
                CounterStatic.increment();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "Thread-3");

        t1.start();
        t2.start();
        t3.start();
    }
}
