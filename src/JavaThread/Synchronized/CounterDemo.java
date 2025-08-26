package JavaThread.Synchronized;

import java.util.ArrayList;
import java.util.List;

public class CounterDemo {
    public static void main(String[] args) throws InterruptedException {
        List<Counter> counters = new ArrayList<>();

        for(int i = 0; i < 4; i++){
            counters.add(new Counter());
        }
        List<Thread> threads = getThreads(counters);

        for(Thread t : threads){
            t.start();
        }
        for (Thread t : threads){
            t.join();
        }

        for(int i = 0; i < counters.size(); i++){
            System.out.println("Counter " + i + ": " + counters.get(i).getCount());
        }
    }

    private static List<Thread> getThreads(List<Counter> counters) {
        List<Thread> threads = new ArrayList<>();

        for(int i = 0; i < 4; i++){
            Counter counter = counters.get(i);
            for(int j = 0; j < 2; j++){
                Thread t = new Thread(()->{
                    for(int k = 0; k < 5; k++){
                        counter.increment();
                        try{
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, "Thread-" + i + "-" + j);
                threads.add(t);
            }
        }
        return threads;
    }

}
