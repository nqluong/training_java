package JavaThread.Thread;

public class MainThread {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Extends Thread Class");
        MyThread t1 = new MyThread("thread1");
        t1.start();

        System.out.println("Implements Runnable Interface");
        Runnable r1 = new MyRunnable("thread2");
        Thread t2 = new Thread(r1);
        t2.start();

        System.out.println("Using Lambda Expression" );
        Thread t3 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("Thread: " + Thread.currentThread().getName() + " - Count: " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "thread3");
        t3.start();

        t1.join();
        t2.join();
        t3.join();
        System.out.println("Main finished");
    }
}
