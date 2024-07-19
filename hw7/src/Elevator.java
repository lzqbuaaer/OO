import com.oocourse.elevator3.DoubleCarResetRequest;
import com.oocourse.elevator3.NormalResetRequest;
import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.ResetRequest;

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
    private int position;
    private int high;
    private int low;
    private int changeFloor;
    private ElevatorShaft shaft;

    public Elevator(int id, RequestTable reqTab, int position, int high,
                    int low, ElevatorShaft shaft, int full, Double rate) {
        this.id = id;
        this.personNum = 0;
        this.towards = true;
        this.person = new HashMap<>();
        this.reqTab = reqTab;
        this.reset = -1;
        this.resetRequest = null;
        this.inReset = false;
        this.full = full;
        this.rate = rate;
        this.reqBuff = null;
        this.schedule = null;
        this.high = high;
        this.low = low;
        this.position = position;
        if (this.position == 1) {
            this.changeFloor = this.high;
            this.floor = high - 1;
        } else if (this.position == 2) {
            this.changeFloor = this.low;
            this.floor = low + 1;
        } else {
            this.changeFloor = -1;
            this.floor = 1;
        }
        this.shaft = shaft;
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
                    this.waitAnother();
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
                    if (this.resetRequest instanceof DoubleCarResetRequest) {
                        break;
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    if (this.floor == this.changeFloor) {
                        this.leaveChangeFloor();
                    }
                    this.reqTab.waitRequest();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        // System.out.println("elevator finish");
    }

    public void waitAnother() throws InterruptedException {
        int to = this.floor;
        if (this.towards) {
            to++;
        } else {
            to--;
        }
        if (to == this.changeFloor) {
            this.shaft.inChangeFloor();
        }
    }

    public void move() throws InterruptedException {
        final boolean flag = this.floor == this.changeFloor;
        if (this.towards) {
            this.floor++;
        } else {
            this.floor--;
        }
        sleep((long) (1000 * this.rate));
        OutPut.arrive(this.floor, this.id, this.position);
        if (this.reset > 0) {
            this.reset--;
        }
        if (flag) {
            this.shaft.outChangeFloor();
        }
    }

    public void openAndClose() throws InterruptedException {
        OutPut.open(this.floor, this.id, this.position);
        this.personOut();
        this.personIn();
        sleep(400);
        this.personIn();
        OutPut.close(this.floor, this.id, this.position);
    }

    public void leaveChangeFloor() throws InterruptedException {
        if (this.position == 2) {
            this.floor++;
        } else {
            this.floor--;
        }
        sleep((long) (1000 * this.rate));
        OutPut.arrive(this.floor, this.id, this.position);
        this.shaft.outChangeFloor();
    }

    public synchronized void personOut() {
        if (this.person.containsKey(this.floor)) {
            ArrayList<PersonRequest> persons = this.person.get(this.floor);
            for (PersonRequest person : persons) {
                OutPut.out(person.getPersonId(), this.floor, this.id, this.position);
                if (person.getToFloor() != floor) {
                    this.schedule.addPersonRequest(new PersonRequest(this.floor,
                            person.getToFloor(), person.getPersonId()));
                } else {
                    Count.sub();
                    this.schedule.stopWait();
                }
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
            OutPut.in(in.getPersonId(), this.floor, this.id, this.position);
        }
    }

    public synchronized void addPerson(PersonRequest personRequest) {
        int floor = personRequest.getToFloor();
        if (floor > this.high) {
            floor = this.high;
        }
        if (floor < this.low) {
            floor = this.low;
        }
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
        if (this.reset < 0) {
            return false;
        } else if (this.reset == 0) {
            return true;
        } else {
            return this.personNum == 0;
        }
    }

    public void resetting() throws InterruptedException {
        if (this.resetRequest instanceof NormalResetRequest) {
            this.normalResetting();
        } else {
            this.doubleResetting();
        }
    }

    public void normalResetting() throws InterruptedException {
        if (this.personNum > 0) {
            OutPut.open(this.floor, this.id, this.position);
            clear();
            sleep(400);
            OutPut.close(this.floor, this.id, this.position);
        }
        OutPut.resetBegin(this.id);
        this.reqTab.clear(this.schedule);
        sleep(1200);
        this.full = ((NormalResetRequest) this.resetRequest).getCapacity();
        this.rate = ((NormalResetRequest) this.resetRequest).getSpeed();
        OutPut.resetEnd(this.id);
        this.setInResetFalse();
        this.reset = -1;
    }

    public void doubleResetting() throws InterruptedException {
        this.reqTab.setStop();
        if (this.personNum > 0) {
            OutPut.open(this.floor, this.id, this.position);
            clear();
            sleep(400);
            OutPut.close(this.floor, this.id, this.position);
        }
        OutPut.resetBegin(this.id);
        this.reqTab.clear(this.schedule);
        DoubleCarElevator elev = this.schedule.setDoubleElevator(
                (DoubleCarResetRequest) this.resetRequest);
        sleep(1200);
        for (PersonRequest req : this.reqBuff.getRequests()) {
            elev.addRequest(req);
        }
        OutPut.resetEnd(this.id);
        elev.endReset();
        this.reqBuff.kill();
    }

    public synchronized void clear() {
        for (int i : this.person.keySet()) {
            ArrayList<PersonRequest> personList = this.person.get(i);
            for (PersonRequest person : personList) {
                OutPut.out(person.getPersonId(), this.floor, this.id, this.position);
                if (person.getToFloor() != floor) {
                    this.schedule.addPersonRequest(
                            new PersonRequest(this.floor, person.getToFloor(),
                                    person.getPersonId()));
                } else {
                    Count.sub();
                    this.schedule.stopWait();
                }
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
