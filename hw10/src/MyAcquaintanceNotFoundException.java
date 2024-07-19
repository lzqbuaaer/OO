import com.oocourse.spec2.exceptions.AcquaintanceNotFoundException;

import java.util.HashMap;

public class MyAcquaintanceNotFoundException extends AcquaintanceNotFoundException {
    private static int count = 0;
    private static final HashMap<Integer, Integer> countId = new HashMap<>();
    private final int id;

    public MyAcquaintanceNotFoundException(int id) {
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
        System.out.println("anf-" + count + ", " + this.id + "-" + countId.get(this.id));
    }
}
