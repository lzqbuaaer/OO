package myexceptions;

import com.oocourse.spec3.exceptions.MessageIdNotFoundException;

import java.util.HashMap;

public class MyMessageIdNotFoundException extends MessageIdNotFoundException {
    private static int count = 0;
    private static final HashMap<Integer, Integer> countId = new HashMap<>();
    private final int id;

    public MyMessageIdNotFoundException(int id) {
        this.id = id;
        count++;
        this.addId(id);
    }

    public void addId(int id) {
        if (countId.containsKey(id)) {
            countId.put(id, countId.get(id) + 1);
        } else {
            countId.put(id, 1);
        }
    }

    @Override
    public void print() {
        System.out.println("minf-" + count + ", " + this.id + "-" + countId.get(this.id));
    }
}
