package DesignPattern.CreationalPattern.Singleton;


/***
    Dung trong don luong, tao ra instance khi goi toi getInstance
    Khong dung duoc trong da luong vi co the tao ra nhieu instance
 ***/
public class LazyInitializedSingleton {
    private static LazyInitializedSingleton instance;

    private LazyInitializedSingleton(){};
    public static LazyInitializedSingleton getInstance(){
        if(instance == null){
            instance = new LazyInitializedSingleton();
        }
        return instance;
    }
}
