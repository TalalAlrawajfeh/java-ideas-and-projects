package callbacks;

import java.util.function.Supplier;

/**
 * Created by u624 on 4/20/17.
 */
public class AsyncCallback<T, R> {
    private final Object monitor = new Object();
    private boolean completed = false;

    public AsyncCallback<T, R> process(Callback<T> callback, Supplier<T> supplier) {
        new Thread(() -> {
            callback.process(supplier.get());
            completed = true;
            synchronized (monitor) {
                monitor.notifyAll();
            }
        }).start();
        return this;
    }

    public AsyncCallback<T, R> onComplete(Callback<R> callback, Supplier<R> supplier) {
        new Thread(() -> {
            while (!completed) {
                synchronized (monitor) {
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            callback.process(supplier.get());
        }).start();
        return this;
    }
}
