import com.oocourse.elevator2.PersonRequest;

import java.util.ArrayList;

public class RequestBuffer extends Thread {
    private final RequestTable requestTable;
    private final ArrayList<PersonRequest> requests;
    private final Elevator elevator;
    private boolean noNew;

    public RequestBuffer(RequestTable requestTable, Elevator elevator) {
        this.requestTable = requestTable;
        this.requests = new ArrayList<>();
        this.noNew = false;
        this.elevator = elevator;
    }

    public void run() {
        do {
            while ((!this.noNew && this.requests.isEmpty()) ||
                    this.elevator.getInReset()) {
                try {
                    beginWait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            addToTable();
        } while (!(this.noNew && requests.isEmpty()));
        this.requestTable.finish();
    }

    public synchronized void finish() {
        this.noNew = true;
        notifyAll();
    }

    public synchronized void addRequest(PersonRequest request) {
        this.requests.add(request);
        notifyAll();
    }

    public synchronized void addToTable() {
        for (PersonRequest request : this.requests) {
            this.requestTable.addRequest(request, true);
        }
        this.requests.clear();

    }

    public synchronized void beginWait() throws InterruptedException {
        wait();
    }

    public synchronized void stopWait() {
        notifyAll();
    }

    public synchronized void copyToTable(RequestTable reqTab) {
        for (PersonRequest request : this.requests) {
            reqTab.addRequest(request, false);
        }
    }
}
