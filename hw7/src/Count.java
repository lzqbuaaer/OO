public class Count {
    private static int cnt = 0;

    public static synchronized void add() {
        cnt++;
    }

    public static synchronized void sub() {
        cnt--;
    }

    public static synchronized boolean zero() {
        return cnt == 0;
    }
}
