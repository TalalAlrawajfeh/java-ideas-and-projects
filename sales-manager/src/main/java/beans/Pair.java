package beans;

import java.io.Serializable;

/**
 * Created by u624 on 3/26/17.
 */
public class Pair<T1, T2> implements Serializable {
    private transient T1 first;
    private transient T2 second;

    public Pair() {
        /* default constructor */
    }

    public Pair(T1 first, T2 second) {
        this.first = first;
        this.second = second;
    }

    public T1 getFirst() {
        return first;
    }

    public void setFirst(T1 first) {
        this.first = first;
    }

    public T2 getSecond() {
        return second;
    }

    public void setSecond(T2 second) {
        this.second = second;
    }
}
