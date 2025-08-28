package JavaThread.Thread;

public class MyThread extends Thread {

    private String threadName;
    public MyThread(String name) {
        this.threadName = name;
    }

    @Override
    public void run() {
        System.out.println("--- Thread " + threadName + " is running ---");
        try {
            for(int i = 0; i < 5; i++) {
                System.out.println("Thread: " + threadName + " - Count: " + i);
                Thread.sleep(1000);
            }
        }catch (InterruptedException e) {
            System.out.println("Thread: " + threadName + " interrupted.");
        }

        System.out.println("--- Thread " + threadName + " is exiting ---");
    }
}
