import com.oocourse.elevator2.PersonRequest;
import com.oocourse.elevator2.ResetRequest;

import java.util.ArrayList;
import java.util.HashMap;

public class Elevator extends Thread {
    private final int id;
    private int personNum;
    private int floor;
    private boolean towards;
    private HashMap<Integer, ArrayList<PersonRequest>> person;
    private RequestTable reqTab;
    private int reset;
    private ResetRequest resetRequest;
    private boolean inReset;
    private int full;
    private double rate;
    private RequestBuffer reqBuff;
    private Schedule schedule;

    public Elevator(int id, RequestTable reqTab) {
        this.id = id;
        this.personNum = 0;
        this.floor = 1;
        this.towards = true;
        this.person = new HashMap<>();
        this.reqTab = reqTab;
        this.reset = -1;
        this.resetRequest = null;
        this.inReset = false;
        this.full = 6;
        this.rate = 0.4;
        this.reqBuff = null;
        this.schedule = null;
    }

    public synchronized int getFull() {
        return this.full;
    }

    public synchronized double getRate() {
        return this.rate;
    }

    public synchronized ResetRequest getResetRequest() {
        return this.resetRequest;
    }

    public synchronized int getPersonNum() {
        return this.personNum;
    }

    public synchronized HashMap<Integer, ArrayList<PersonRequest>> getPerson() {
        return this.person;
    }

    public synchronized RequestTable getReqTab() {
        return this.reqTab;
    }

    public synchronized RequestBuffer getReqBuff() {
        return this.reqBuff;
    }

    public synchronized void setReqBuff(RequestBuffer buffer) {
        this.reqBuff = buffer;
    }

    public synchronized void setSchedule(Schedule schedule) {
        this.schedule = schedule;
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
            } else if (action == Action.RESET) {
                try {
                    this.resetting();
                    this.reqTab.stopReset();
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
        sleep((long) (1000 * this.rate));
        OutPut.arrive(this.floor, this.id);
        if (this.reset > 0) {
            this.reset--;
        }
    }

    public void openAndClose() throws InterruptedException {
        OutPut.open(this.floor, this.id);
        this.personOut();
        this.personIn();
        sleep(400);
        this.personIn();
        OutPut.close(this.floor, this.id);
    }

    public synchronized void personOut() {
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
        while (this.personNum < this.full) {
            int order = this.reqTab.canIn(this.floor, this.towards);
            if (order == -1) {
                break;
            }
            PersonRequest in = this.reqTab.removePerson(this.floor, order);
            this.addPerson(in);
            OutPut.in(in.getPersonId(), this.floor, this.id);
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
        this.personNum++;
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
        if (this.personNum >= this.full) {
            return false;
        }
        return this.reqTab.canIn(this.floor, this.towards) != -1;
    }

    public Boolean isEmpty() {
        return personNum == 0;
    }

    public synchronized void beginReset(ResetRequest request) {
        this.reset = 1;
        this.resetRequest = request;
        this.setInResetTrue();
        this.reqTab.beginReset();
    }

    public Boolean needReset() {
        if (this.reset == 0) {
            return true;
        } else if (this.reset > 0) {
            return this.personNum == 0;
        } else {
            return false;
        }
    }

    public void resetting() throws InterruptedException {
        if (this.personNum > 0) {
            OutPut.open(this.floor, this.id);
            clear();
            sleep(400);
            OutPut.close(this.floor, this.id);
        }
        OutPut.resetBegin(this.id);
        this.reqTab.clear(this.schedule);
        sleep(1200);
        this.full = this.resetRequest.getCapacity();
        this.rate = this.resetRequest.getSpeed();
        OutPut.resetEnd(this.id);
        this.setInResetFalse();
        this.reset = -1;
    }

    public synchronized void clear() {
        for (int i : this.person.keySet()) {
            ArrayList<PersonRequest> personList = this.person.get(i);
            for (PersonRequest person : personList) {
                OutPut.out(person.getPersonId(), this.floor, this.id);
                this.schedule.addPersonRequest(
                        new PersonRequest(this.floor, person.getToFloor(), person.getPersonId()));
            }
            personList.clear();
        }
        this.personNum = 0;
    }

    public synchronized void setInResetTrue() {
        this.inReset = true;
    }

    public synchronized void setInResetFalse() {
        this.inReset = false;
        this.reqBuff.stopWait();
        this.schedule.stopWait();
    }

    public boolean getInReset() {
        return this.inReset;
    }
}
