import com.oocourse.spec2.exceptions.EqualRelationException;

import java.util.HashMap;

public class MyEqualRelationException extends EqualRelationException {
    private static int count = 0;
    private static final HashMap<Integer, Integer> countId = new HashMap<>();
    private final int id1;
    private final int id2;

    public MyEqualRelationException(int id1, int id2) {
        this.id1 = id1;
        this.id2 = id2;
        count++;
        this.addId(id1);
        if (id1 != id2) {
            this.addId(id2);
        }
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
        int cnt1 = countId.get(this.id1);
        int cnt2 = countId.get(this.id2);
        if (this.id1 > this.id2) {
            System.out.println("er-" + count + ", " +
                    this.id2 + "-" + cnt2 + ", " + this.id1 + "-" + cnt1);
        } else {
            System.out.println("er-" + count + ", " +
                    this.id1 + "-" + cnt1 + ", " + this.id2 + "-" + cnt2);
        }
    }
}
