package DesignPattern.CreationalPattern.Singleton;

public class MainSingleton {
    public static void main(String[] args) throws InterruptedException {
        EagerInitializedSingleton instance1 = EagerInitializedSingleton.getInstance();
        EagerInitializedSingleton instance2 = EagerInitializedSingleton.getInstance();

        System.out.println("Instance1 vs Instance2 = " + (instance1 == instance2)); // true

        LazyInitializedSingleton instance3 = LazyInitializedSingleton.getInstance();
        LazyInitializedSingleton instance4 = LazyInitializedSingleton.getInstance();

        System.out.println("Instance3 vs Instance4 = " + (instance3 == instance4)); // true

        Runnable task = () -> {
            ThreadSafeSingleton instance = ThreadSafeSingleton.getInstance();
            System.out.println("Instance from thread " + Thread.currentThread().getName() + ": " + instance);
        };

        Thread t1 = new Thread(task, "T1");
        Thread t2 = new Thread(task, "T2");
        Thread t3 = new Thread(task, "T3");

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

//        Instance from thread T1: DesignPattern.CreationalPattern.Singleton.ThreadSafeSingleton@48074628
//        Instance from thread T3: DesignPattern.CreationalPattern.Singleton.ThreadSafeSingleton@48074628
//        Instance from thread T2: DesignPattern.CreationalPattern.Singleton.ThreadSafeSingleton@48074628
    }
}
