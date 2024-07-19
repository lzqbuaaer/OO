import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.ResetRequest;

public interface ElevatorShaft {
    void addRequest(PersonRequest request);

    void finish();

    boolean getInReset();

    void setSchedule(Schedule schedule);

    void beginReset(ResetRequest request);

    void inChangeFloor() throws InterruptedException;

    void outChangeFloor();

    int cost(PersonRequest request);
}
