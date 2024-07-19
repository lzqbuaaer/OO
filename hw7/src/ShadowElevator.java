import com.oocourse.elevator3.DoubleCarResetRequest;
import com.oocourse.elevator3.NormalResetRequest;
import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.ResetRequest;

import java.util.ArrayList;
import java.util.HashMap;

public class ShadowElevator {
    private int personNum;
    private int floor;
    private boolean towards;
    private HashMap<Integer, ArrayList<PersonRequest>> person;
    private RequestTable reqTab;
    private int full;
    private double rate;
    private int cost;
    private PersonRequest target;

    public ShadowElevator(Elevator elevator, PersonRequest target) {
        this.floor = elevator.getFloor();
        this.towards = elevator.getTowards();
        if (elevator.getInReset()) {
            this.personNum = 0;
            ResetRequest req = elevator.getResetRequest();
            if (req instanceof NormalResetRequest) {
                this.full = ((NormalResetRequest) req).getCapacity();
                this.rate = ((NormalResetRequest) req).getSpeed();
            } else {
                if (req != null) {
                    this.full = ((DoubleCarResetRequest) req).getCapacity();
                    this.rate = ((DoubleCarResetRequest) req).getSpeed();
                } else {
                    this.full = elevator.getFull();
                    this.rate = elevator.getRate();
                }
            }
            this.cost = 600;
            this.reqTab = new RequestTable(elevator.getReqTab().getId(), 11, 1, 0);
            elevator.getReqBuff().copyToTable(this.reqTab);
            this.person = new HashMap<>();
        } else {
            this.personNum = elevator.getPersonNum();
            this.full = elevator.getFull();
            this.rate = elevator.getRate();
            this.cost = 0;
            copyPerson(elevator.getPerson());
            this.reqTab = elevator.getReqTab().deepCopy();
            elevator.getReqBuff().copyToTable(this.reqTab);
        }
        this.reqTab.addRequest(target, false);
        this.target = target;
    }

    public synchronized void copyPerson(HashMap<Integer, ArrayList<PersonRequest>> list) {
        this.person = new HashMap<>();
        for (Integer key : list.keySet()) {
            ArrayList<PersonRequest> originalList = list.get(key);
            ArrayList<PersonRequest> clonedList = new ArrayList<>(originalList);
            this.person.put(key, clonedList);
        }
    }

    public synchronized int getFloor() {
        return floor;
    }

    public synchronized Boolean getTowards() {
        return towards;
    }

    public synchronized Boolean needOut() {
        if (this.person.containsKey(this.floor)) {
            return !this.person.get(this.floor).isEmpty();
        }
        return false;
    }

    public synchronized Boolean canIn() {
        if (this.personNum >= this.full) {
            return false;
        }
        return this.reqTab.canIn(this.floor, this.towards) != -1;
    }

    public synchronized Boolean isEmpty() {
        return personNum == 0;
    }

    public int run() {
        while (true) {
            Action action = Strategy.getShadowAction(this, this.reqTab);
            if (action == Action.REVERSE) {
                this.towards = !this.towards;
            } else if (action == Action.MOVE) {
                this.move();
            } else if (action == Action.OPEN) {
                this.cost += 400;
                this.personOut();
                this.personIn();
            } else {
                break;
            }
        }
        return this.cost;
    }

    public synchronized void move() {
        if (this.towards) {
            this.floor++;
        } else {
            this.floor--;
        }
        this.cost += (int) (1000 * this.rate);
    }

    public synchronized void personOut() {
        if (this.person.containsKey(this.floor)) {
            ArrayList<PersonRequest> persons = this.person.get(this.floor);
            this.personNum -= persons.size();
            persons.clear();
        }
    }

    public synchronized void personIn() {
        while (this.personNum < this.full) {
            int order = this.reqTab.canIn(this.floor, this.towards);
            if (order == -1) {
                break;
            }
            this.personNum++;
            PersonRequest in = this.reqTab.removePerson(this.floor, order);
            this.addPerson(in);
        }
    }

    public synchronized void addPerson(PersonRequest personRequest) {
        int floor = personRequest.getToFloor();
        if (this.person.containsKey(floor)) {
            this.person.get(floor).add(personRequest);
        } else {
            ArrayList<PersonRequest> newRequestSet = new ArrayList<>();
            newRequestSet.add(personRequest);
            this.person.put(floor, newRequestSet);
        }
    }

    public synchronized boolean targetFinish(PersonRequest target) {
        if (this.floor != target.getToFloor()) {
            return false;
        }
        ArrayList<PersonRequest> list = this.person.get(target.getToFloor());
        boolean inElevator = list != null && list.contains(target);
        return inElevator && this.reqTab.has(target);
    }
}
