import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.ResetRequest;

public class NormalElevator implements ElevatorShaft {
    private int id;
    private Schedule schedule;
    private Elevator elevator;
    private RequestTable requestTable;
    private RequestBuffer requestBuffer;

    public NormalElevator(int id) {
        this.id = id;
        this.requestTable = new RequestTable(id, 11, 1, 0);
        this.elevator = new Elevator(id, this.requestTable, 0, 11, 1, this, 6, 0.4);
        this.requestBuffer = new RequestBuffer(this.requestTable, this.elevator);
        this.elevator.setReqBuff(this.requestBuffer);
        this.elevator.start();
        this.requestBuffer.start();
    }

    @Override
    public void addRequest(PersonRequest request) {
        this.requestBuffer.addRequest(request);
    }

    @Override
    public void finish() {
        this.requestBuffer.finish();
    }

    @Override
    public boolean getInReset() {
        return this.elevator.getInReset();
    }

    @Override
    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
        this.elevator.setSchedule(schedule);
    }

    @Override
    public void beginReset(ResetRequest request) {
        this.elevator.beginReset(request);
    }

    @Override
    public void inChangeFloor() {
    }

    @Override
    public void outChangeFloor() {
    }

    @Override
    public int cost(PersonRequest request) {
        ShadowElevator shElev = new ShadowElevator(this.elevator, request);
        return shElev.run();
    }

    public RequestBuffer getRequestBuffer() {
        return this.requestBuffer;
    }
}
