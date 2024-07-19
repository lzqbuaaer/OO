import com.oocourse.elevator3.PersonRequest;

import java.util.ArrayList;
import java.util.HashMap;

public class RequestTable {
    private final int id;
    private int num;
    private final HashMap<Integer, ArrayList<PersonRequest>> request;
    private Boolean noNew;
    private Boolean resetting;
    private int high;
    private int low;
    private int position;
    private boolean stop;

    public RequestTable(int id, int high, int low, int position) {
        this.num = 0;
        this.request = new HashMap<>();
        this.noNew = false;
        this.id = id;
        this.resetting = false;
        this.high = high;
        this.low = low;
        this.position = position;
        this.stop = false;
    }

    public RequestTable(int id, int num, HashMap<Integer, ArrayList<PersonRequest>> request,
                        Boolean noNew, Boolean resetting, int high, int low, int position) {
        this.num = num;
        this.noNew = noNew;
        this.id = id;
        this.resetting = resetting;
        this.request = new HashMap<>();
        this.high = high;
        this.low = low;
        this.position = position;
        for (Integer key : request.keySet()) {
            ArrayList<PersonRequest> originalList = request.get(key);
            ArrayList<PersonRequest> clonedList = new ArrayList<>(originalList);
            this.request.put(key, clonedList);
        }
    }

    public synchronized RequestTable deepCopy() {
        return new RequestTable(this.id, this.num, this.request, this.noNew,
                this.resetting, 11, 1, 0);
    }

    public synchronized Boolean isEnd() {
        return this.noNew && this.num == 0;
    }

    public synchronized void addRequest(PersonRequest personRequest, Boolean mark) {
        int floor = personRequest.getFromFloor();
        if (this.request.containsKey(floor)) {
            this.request.get(floor).add(personRequest);
        } else {
            ArrayList<PersonRequest> newRequestSet = new ArrayList<>();
            newRequestSet.add(personRequest);
            this.request.put(floor, newRequestSet);
        }
        if (mark && !this.stop) {
            OutPut.receive(personRequest.getPersonId(), this.id, this.position);
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
            for (int f = floor + 1; f <= this.high; f++) {
                if (this.request.containsKey(f)) {
                    if (!this.request.get(f).isEmpty()) {
                        return true;
                    }
                }
            }
        } else {
            for (int f = floor - 1; f >= this.low; f--) {
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
        while (!this.noNew && this.isEmpty() && !this.resetting) {
            this.wait();
        }
    }

    public synchronized void beginReset() {
        this.resetting = true;
        this.notifyAll();
    }

    public synchronized void stopReset() {
        this.resetting = false;
    }

    public synchronized boolean has(PersonRequest person) {
        ArrayList<PersonRequest> list = this.request.get(person.getFromFloor());
        return list.contains(person);
    }

    public synchronized void clear(Schedule schedule) {
        for (Integer key : this.request.keySet()) {
            ArrayList<PersonRequest> list = request.get(key);
            for (PersonRequest person : list) {
                schedule.addPersonRequest(person);
            }
            list.clear();
        }
        this.num = 0;
    }

    public synchronized int getId() {
        return this.id;
    }

    public synchronized void setStop() {
        this.stop = true;
    }

    public synchronized int getNum() {
        return this.num;
    }
}
