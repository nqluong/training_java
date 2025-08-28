package DesignPattern.CreationalPattern.Singleton;


/***
 * Thread-Safe Singleton Pattern
 * Co the dung trong moi truong da luong, bao dam chi co the co 1 luong vao duoc instance
 */
public class ThreadSafeSingleton {
    private static ThreadSafeSingleton instance;

    private ThreadSafeSingleton() {
    }

    public static ThreadSafeSingleton getInstance() {
        if (instance == null) {
            synchronized (ThreadSafeSingleton.class) {
                if (instance == null) {
                    instance = new ThreadSafeSingleton();
                }
            }
        }
        return instance;
    }
}
