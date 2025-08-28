package DesignPattern.CreationalPattern.Singleton;

/***
 * Khoi tao instance ngay khi class duoc nap mac du khong dung toi
 */

public class EagerInitializedSingleton  {
    private static final EagerInitializedSingleton instance = new EagerInitializedSingleton();

    private EagerInitializedSingleton(){}

    public static EagerInitializedSingleton getInstance(){
        return instance;
    }
}
