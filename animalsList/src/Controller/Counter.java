package Controller;

public class Counter implements AutoCloseable {

    static int sum;
    {
        sum = 0;
    }

    public void add() {
        sum++;
    }

    public void sub() {
        sum--;
    }

    @Override
    public void close() {
        System.out.println("Counter closed");
    }
}