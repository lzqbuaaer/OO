import com.oocourse.elevator3.PersonRequest;

import java.util.ArrayList;

public class RequestBuffer extends Thread {
    private final RequestTable requestTable;
    private final ArrayList<PersonRequest> requests;
    private final Elevator elevator;
    private boolean noNew;
    private boolean needStop;

    public RequestBuffer(RequestTable requestTable, Elevator elevator) {
        this.requestTable = requestTable;
        this.requests = new ArrayList<>();
        this.noNew = false;
        this.elevator = elevator;
        this.needStop = false;
    }

    public void run() {
        do {
            while (((!this.noNew && this.requests.isEmpty()) ||
                    this.elevator.getInReset()) && !needStop) {
                try {
                    beginWait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            addToTable();
        } while (!(this.noNew && requests.isEmpty() && Count.zero()) && !needStop);
        this.requestTable.finish();
        // System.out.println("Buffer finish");
    }

    public synchronized void kill() {
        this.needStop = true;
        notifyAll();
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

    public synchronized ArrayList<PersonRequest> getRequests() {
        return this.requests;
    }

    public int getPersonNum() {
        return this.requests.size();
    }
}
