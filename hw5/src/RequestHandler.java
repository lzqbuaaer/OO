import com.oocourse.elevator1.PersonRequest;

import java.util.ArrayList;

public class RequestHandler {
    private ArrayList<RequestTable> reqTabs;

    public RequestHandler(ArrayList<RequestTable> reqTabs) {
        this.reqTabs = reqTabs;
    }

    public void addRequest(PersonRequest request) {
        int id = request.getElevatorId();
        this.reqTabs.get(id - 1).addRequest(request);
    }

    public void finish() {
        for (RequestTable reqTab : this.reqTabs) {
            reqTab.finish();
        }
    }
}
