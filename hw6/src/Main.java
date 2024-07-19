import com.oocourse.elevator2.PersonRequest;
import com.oocourse.elevator2.TimableOutput;
import com.oocourse.elevator2.ResetRequest;
import com.oocourse.elevator2.ElevatorInput;
import com.oocourse.elevator2.Request;

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
                } else if (request instanceof ResetRequest) {
                    requestHandler.addResetRequest((ResetRequest) request);
                }
            }
        }
        elevatorInput.close();
        requestHandler.finish();
    }

    private static RequestHandler getRequestHandler() {
        ArrayList<Elevator> elevators = new ArrayList<>();
        ArrayList<RequestBuffer> reqBuffs = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            RequestTable reqTab = new RequestTable(i);
            Elevator elev = new Elevator(i, reqTab);
            RequestBuffer reqBuff = new RequestBuffer(reqTab, elev);
            elev.setReqBuff(reqBuff);
            elevators.add(elev);
            reqBuffs.add(reqBuff);
            elev.start();
            reqBuff.start();
        }
        Schedule schedule = new Schedule(elevators, reqBuffs);
        schedule.start();
        return new RequestHandler(elevators, schedule);
    }
}

