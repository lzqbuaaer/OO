import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.DoubleCarResetRequest;
import com.oocourse.elevator3.NormalResetRequest;
import com.oocourse.elevator3.TimableOutput;
import com.oocourse.elevator3.ElevatorInput;
import com.oocourse.elevator3.Request;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws Exception {
        TimableOutput.initStartTimestamp();
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        RequestHandler requestHandler = getRequestHandler();
        while (true) {
            Request request = elevatorInput.nextRequest();
            if (request == null) {
                break;
            } else {
                if (request instanceof PersonRequest) {
                    requestHandler.addPersonRequest((PersonRequest) request);
                    Count.add();
                } else if (request instanceof NormalResetRequest) {
                    requestHandler.addNormalResetRequest((NormalResetRequest) request);
                } else {
                    requestHandler.addDoubleResetRequest((DoubleCarResetRequest) request);
                }
            }
        }
        elevatorInput.close();
        requestHandler.finish();
    }

    private static RequestHandler getRequestHandler() {
        ArrayList<ElevatorShaft> elevators = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            NormalElevator elevator = new NormalElevator(i);
            elevators.add(elevator);
        }
        Schedule schedule = new Schedule(elevators);
        schedule.start();
        return new RequestHandler(elevators, schedule);
    }
}

