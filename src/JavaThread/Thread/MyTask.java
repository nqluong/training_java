package JavaThread.Thread;

import java.util.concurrent.Callable;

public class MyTask implements Callable<String> {

    private String threadName;

    public MyTask(String threadName) {
        this.threadName = threadName;
    }
    @Override
    public String call() throws Exception {
        System.out.println("Thread " + threadName + " is running.");
        for(int i = 0 ; i < 5; i++) {
            System.out.println("Thread " + threadName + " - Count: " + i);
            Thread.sleep(1000);
        }
        System.out.println("Thread " + threadName + " has finished.");
        return "Task of " + threadName + " completed.";
    }
}
