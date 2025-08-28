package JavaThread.Thread;

import java.util.Random;
import java.util.concurrent.Callable;

public class CallableWorker implements Callable<Integer> {

    private int num;
    private Random random;

    public CallableWorker(int num) {
        this.num = num;
        random = new Random();
    }

    public Integer call() throws Exception {
        int sleepTime = random.nextInt(1000, 5000);
        System.out.println("Worker " + num + " is sleeping for " + sleepTime + " ms");
        Thread.sleep(sleepTime);
        System.out.println("Worker " + num + " has finished sleeping");
        return num * num;
    }
}
