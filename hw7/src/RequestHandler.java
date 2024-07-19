import com.oocourse.elevator3.DoubleCarResetRequest;
import com.oocourse.elevator3.NormalResetRequest;
import com.oocourse.elevator3.PersonRequest;

import java.util.ArrayList;

public class RequestHandler {
    private ArrayList<ElevatorShaft> elevators;
    private Schedule schedule;

    public RequestHandler(ArrayList<ElevatorShaft> elevators, Schedule schedule) {
        this.elevators = elevators;
        this.schedule = schedule;
        for (ElevatorShaft elevator : elevators) {
            elevator.setSchedule(schedule);
        }
    }

    public void addPersonRequest(PersonRequest request) {
        schedule.addPersonRequest(request);
    }

    public void addNormalResetRequest(NormalResetRequest request) {
        int id = request.getElevatorId();
        this.elevators.get(id - 1).beginReset(request);
    }

    public void addDoubleResetRequest(DoubleCarResetRequest request) {
        int id = request.getElevatorId();
        this.elevators.get(id - 1).beginReset(request);
    }

    public void finish() {
        this.schedule.finish();
    }
}
