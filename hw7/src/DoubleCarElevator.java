import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.ResetRequest;

public class DoubleCarElevator implements ElevatorShaft {
    private int id;
    private int changeFloor;
    private Elevator elevatorA;
    private RequestTable requestTableA;
    private RequestBuffer requestBufferA;
    private Elevator elevatorB;
    private RequestTable requestTableB;
    private RequestBuffer requestBufferB;
    private Schedule schedule;
    private boolean canInChangeFloor;

    public DoubleCarElevator(int id, int floor, int full, Double rate) {
        this.id = id;
        this.changeFloor = floor;
        this.canInChangeFloor = true;
        this.requestTableA = new RequestTable(id, floor, 1, 1);
        this.elevatorA = new Elevator(id, this.requestTableA, 1, floor, 1, this, full, rate);
        this.requestBufferA = new RequestBuffer(this.requestTableA, this.elevatorA);
        this.elevatorA.setReqBuff(this.requestBufferA);
        this.requestTableB = new RequestTable(id, 11, floor, 2);
        this.elevatorB = new Elevator(id, this.requestTableB, 2, 11, floor, this, full, rate);
        this.requestBufferB = new RequestBuffer(this.requestTableB, this.elevatorB);
        this.elevatorB.setReqBuff(this.requestBufferB);
        this.elevatorA.setInResetTrue();
        this.elevatorB.setInResetTrue();
        this.elevatorA.start();
        this.requestBufferA.start();
        this.elevatorB.start();
        this.requestBufferB.start();
    }

    @Override
    public void addRequest(PersonRequest request) {
        if (request.getFromFloor() < this.changeFloor) {
            this.requestBufferA.addRequest(request);
        } else if (request.getFromFloor() == this.changeFloor &&
                request.getToFloor() < this.changeFloor) {
            this.requestBufferA.addRequest(request);
        } else {
            this.requestBufferB.addRequest(request);
        }
    }

    @Override
    public void finish() {
        this.requestBufferA.finish();
        this.requestBufferB.finish();
    }

    @Override
    public boolean getInReset() {
        return false;
    }

    @Override
    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
        this.elevatorA.setSchedule(schedule);
        this.elevatorB.setSchedule(schedule);
    }

    @Override
    public void beginReset(ResetRequest request) {
    }

    @Override
    public synchronized void inChangeFloor() throws InterruptedException {
        while (!this.canInChangeFloor) {
            wait();
        }
        this.canInChangeFloor = false;
    }

    public synchronized void outChangeFloor() {
        this.canInChangeFloor = true;
        notifyAll();
    }

    @Override
    public int cost(PersonRequest request) {
        int floor = request.getFromFloor();
        if (floor < this.changeFloor) {
            ShadowElevator shElev = new ShadowElevator(this.elevatorA, request);
            return shElev.run();
        } else if (floor == this.changeFloor &&
                request.getToFloor() < this.changeFloor) {
            ShadowElevator shElev = new ShadowElevator(this.elevatorA, request);
            return shElev.run();
        } else {
            ShadowElevator shElev = new ShadowElevator(this.elevatorB, request);
            return shElev.run();
        }
    }

    public void endReset() {
        this.elevatorA.setInResetFalse();
        this.elevatorB.setInResetFalse();
    }
}
