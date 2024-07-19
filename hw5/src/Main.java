import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;
import com.oocourse.elevator1.TimableOutput;

import java.util.ArrayList;

class Main {
    public static void main(String[] args) throws Exception {
        TimableOutput.initStartTimestamp();
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        ArrayList<RequestTable> reqTabs = new ArrayList<>();
        ArrayList<Elevator> elevators = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            RequestTable reqTab = new RequestTable(i);
            Elevator elev = new Elevator(i, reqTab);
            reqTabs.add(reqTab);
            elevators.add(elev);
            elev.start();
        }
        RequestHandler requestHandler = new RequestHandler(reqTabs);
        while (true) {
            PersonRequest request = elevatorInput.nextPersonRequest();
            if (request == null) {
                break;
            }
            requestHandler.addRequest(request);
        }
        elevatorInput.close();
        requestHandler.finish();
    }
}
