import com.oocourse.elevator2.PersonRequest;

import java.util.ArrayList;

public class Schedule extends Thread {
    private ArrayList<Elevator> elevators;
    private ArrayList<RequestBuffer> reqBuffs;
    private ArrayList<PersonRequest> input;
    private boolean noInput;

    public Schedule(ArrayList<Elevator> elevators, ArrayList<RequestBuffer> reqBuffs) {
        this.elevators = elevators;
        this.reqBuffs = reqBuffs;
        this.input = new ArrayList<>();
        this.noInput = false;
    }

    public void run() {
        while (!(this.noInput && this.input.isEmpty() && noReset())) {
            while (this.input.isEmpty()) {
                try {
                    if (this.noInput && noReset()) {
                        break;
                    }
                    beginWait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            if (!this.input.isEmpty()) {
                PersonRequest person = getOne();
                getSchedule(person).addRequest(person);
            }
        }
        for (RequestBuffer reqBuff : this.reqBuffs) {
            reqBuff.finish();
        }
    }

    public synchronized PersonRequest getOne() {
        return this.input.remove(0);
    }

    public RequestBuffer getSchedule(PersonRequest request) {
        int ans = 0;
        int cost = 999999999;
        for (int i = 0; i < 6; i++) {
            ShadowElevator shElev = new ShadowElevator(this.elevators.get(i), request);
            int costI = shElev.run();
            if (cost > costI) {
                cost = costI;
                ans = i;
            }
        }
        return this.reqBuffs.get(ans);
    }

    public synchronized void addPersonRequest(PersonRequest request) {
        this.input.add(request);
        notifyAll();
    }

    public synchronized boolean noReset() {
        for (Elevator elevator : elevators) {
            if (elevator.getInReset()) {
                return false;
            }
        }
        return true;
    }

    public synchronized void finish() {
        this.noInput = true;
        notifyAll();
    }

    public synchronized void stopWait() {
        notifyAll();
    }

    public synchronized void beginWait() throws InterruptedException {
        wait();
    }
}
