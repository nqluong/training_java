package JavaThread.Thread;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutorServiceDemo {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        Future<String> future1 = executor.submit(new MyTask("Task-1"));
        Future<String> future2 = executor.submit(new MyTask("Task-2"));
        Future<String> future3 = executor.submit(()->{
            System.out.println("Thread: " + Thread.currentThread().getName() + " is running.");
            for(int i = 1; i <= 5; i++) {
                System.out.println("Task-3 - Count: " + i);
                Thread.sleep(100);
            }
            System.out.println("Thread: " + Thread.currentThread().getName() + " has completed.");
            return "Task-3 Completed";
        });

        System.out.println("Main thread is doing other work...");
        try {
            String result1 = future1.get();
            String result2 = future2.get();
            String result3 = future3.get();
            System.out.println("Results: " + result1 + ", " + result2 + ", " + result3);
        }catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        executor.shutdown();
        System.out.println("All tasks submitted.");
    }
}
