import com.oocourse.elevator2.PersonRequest;
import com.oocourse.elevator2.ResetRequest;

import java.util.ArrayList;

public class RequestHandler {
    private ArrayList<Elevator> elevators;
    private Schedule schedule;

    public RequestHandler(ArrayList<Elevator> elevators, Schedule schedule) {
        this.elevators = elevators;
        this.schedule = schedule;
        for (Elevator elevator : elevators) {
            elevator.setSchedule(schedule);
        }
    }

    public void addPersonRequest(PersonRequest request) {
        schedule.addPersonRequest(request);
    }

    public void addResetRequest(ResetRequest request) {
        int id = request.getElevatorId();
        this.elevators.get(id - 1).beginReset(request);
    }

    public void finish() {
        this.schedule.finish();
    }
}
