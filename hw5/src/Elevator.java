import com.oocourse.elevator1.PersonRequest;

import java.util.ArrayList;
import java.util.HashMap;

public class Elevator extends Thread {
    private final int id;
    private int personNum;
    private int floor;
    private Boolean towards;
    private HashMap<Integer, ArrayList<PersonRequest>> person;
    private RequestTable reqTab;

    public Elevator(int id, RequestTable reqTab) {
        this.id = id;
        this.personNum = 0;
        this.floor = 1;
        this.towards = true;
        this.person = new HashMap<>();
        this.reqTab = reqTab;
    }

    @Override
    public void run() {
        while (true) {
            Action action = Strategy.getAction(this, this.reqTab);
            if (action == Action.OVER) {
                break;
            } else if (action == Action.REVERSE) {
                this.towards = !this.towards;
            } else if (action == Action.MOVE) {
                try {
                    this.move();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else if (action == Action.OPEN) {
                try {
                    this.openAndClose();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    this.reqTab.waitRequest();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void move() throws InterruptedException {
        if (this.towards) {
            this.floor++;
        } else {
            this.floor--;
        }
        sleep(400);
        OutPut.arrive(this.floor, this.id);
    }

    public void openAndClose() throws InterruptedException {
        OutPut.open(this.floor, this.id);
        this.personOut();
        this.personIn();
        sleep(400);
        this.personIn();
        OutPut.close(this.floor, this.id);
    }

    public void personOut() {
        if (this.person.containsKey(this.floor)) {
            ArrayList<PersonRequest> persons = this.person.get(this.floor);
            for (PersonRequest person : persons) {
                OutPut.out(person.getPersonId(), this.floor, this.id);
            }
            this.personNum -= persons.size();
            persons.clear();
        }
    }

    public void personIn() {
        while (this.personNum < 6) {
            int order = this.reqTab.canIn(this.floor, this.towards);
            if (order == -1) {
                break;
            }
            this.personNum++;
            PersonRequest in = this.reqTab.removePerson(this.floor, order);
            this.addPerson(in);
            OutPut.in(in.getPersonId(), this.floor, this.id);
        }
    }

    public void addPerson(PersonRequest personRequest) {
        int floor = personRequest.getToFloor();
        if (this.person.containsKey(floor)) {
            this.person.get(floor).add(personRequest);
        } else {
            ArrayList<PersonRequest> newRequestSet = new ArrayList<>();
            newRequestSet.add(personRequest);
            this.person.put(floor, newRequestSet);
        }
    }

    public int getFloor() {
        return floor;
    }

    public Boolean getTowards() {
        return towards;
    }

    public Boolean needOut() {
        if (this.person.containsKey(this.floor)) {
            return !this.person.get(this.floor).isEmpty();
        }
        return false;
    }

    public Boolean canIn() {
        if (this.personNum >= 6) {
            return false;
        }
        return this.reqTab.canIn(this.floor, this.towards) != -1;
    }

    public Boolean isEmpty() {
        return personNum == 0;
    }
}
