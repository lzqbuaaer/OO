import com.oocourse.elevator1.PersonRequest;

import java.util.ArrayList;
import java.util.HashMap;

public class RequestTable {
    private final int id;
    private int num;
    private HashMap<Integer, ArrayList<PersonRequest>> request;
    private Boolean noNew;

    public RequestTable(int id) {
        this.num = 0;
        this.request = new HashMap<>();
        this.noNew = false;
        this.id = id;
    }

    public synchronized Boolean isEnd() {
        return this.noNew && this.num == 0;
    }

    public synchronized void addRequest(PersonRequest personRequest) {
        int floor = personRequest.getFromFloor();
        if (this.request.containsKey(floor)) {
            this.request.get(floor).add(personRequest);
        } else {
            ArrayList<PersonRequest> newRequestSet = new ArrayList<>();
            newRequestSet.add(personRequest);
            this.request.put(floor, newRequestSet);
        }
        this.num++;
        this.notifyAll();
    }

    public synchronized void finish() {
        this.noNew = true;
        this.notifyAll();
    }

    public synchronized int canIn(int floor, Boolean towards) {
        if (this.request.containsKey(floor)) {
            ArrayList<PersonRequest> persons = this.request.get(floor);
            for (int i = 0; i < persons.size(); i++) {
                if ((persons.get(i).getToFloor() > floor) == towards) {
                    return i;
                }
            }
        }
        return -1;
    }

    public synchronized Boolean isEmpty() {
        return this.num == 0;
    }

    public synchronized Boolean towardsHasPerson(int floor, Boolean towards) {
        if (towards) {
            for (int f = floor + 1; f <= 11; f++) {
                if (this.request.containsKey(f)) {
                    if (!this.request.get(f).isEmpty()) {
                        return true;
                    }
                }
            }
        } else {
            for (int f = floor - 1; f >= 1; f--) {
                if (this.request.containsKey(f)) {
                    if (!this.request.get(f).isEmpty()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public synchronized PersonRequest removePerson(int floor, int order) {
        this.num--;
        return this.request.get(floor).remove(order);
    }

    public synchronized void waitRequest() throws InterruptedException {
        while (!this.noNew && this.isEmpty()) {
            this.wait();
        }
    }
}
