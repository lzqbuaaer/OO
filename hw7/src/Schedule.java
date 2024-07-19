import com.oocourse.elevator3.DoubleCarResetRequest;
import com.oocourse.elevator3.PersonRequest;

import java.util.ArrayList;

public class Schedule extends Thread {
    private ArrayList<ElevatorShaft> elevators;
    private ArrayList<PersonRequest> input;
    private boolean noInput;
    private int cnt;

    public Schedule(ArrayList<ElevatorShaft> elevators) {
        this.elevators = elevators;
        this.input = new ArrayList<>();
        this.noInput = false;
        this.cnt = 0;
    }

    public void run() {
        while (!(this.noInput && this.input.isEmpty() && Count.zero()
                && noReset())) {
            while (this.input.isEmpty()) {
                try {
                    if (this.noInput && noReset() && Count.zero()) {
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
        for (ElevatorShaft elevator : this.elevators) {
            elevator.finish();
        }
    }

    public synchronized PersonRequest getOne() {
        return this.input.remove(0);
    }

    public ElevatorShaft getSchedule(PersonRequest request) {
        ElevatorShaft ans = null;
        int value = 99999;
        for (ElevatorShaft elev : this.elevators) {
            int cost = elev.cost(request);
            if (value > cost) {
                ans = elev;
                value = cost;
            }
        }
        return ans;
    }

    public synchronized void addPersonRequest(PersonRequest request) {
        this.input.add(request);
        notifyAll();
    }

    public synchronized boolean noReset() {
        for (ElevatorShaft elevator : elevators) {
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

    public synchronized DoubleCarElevator setDoubleElevator(DoubleCarResetRequest request) {
        int i = request.getElevatorId();
        DoubleCarElevator elev = new DoubleCarElevator(i,
                request.getTransferFloor(), request.getCapacity(), request.getSpeed());
        this.elevators.set(i - 1, elev);
        this.elevators.get(i - 1).setSchedule(this);
        return elev;
    }
}
