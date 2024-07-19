import com.oocourse.elevator1.TimableOutput;

public class OutPut {
    public static void arrive(int floor, int id) {
        TimableOutput.println("ARRIVE-" + floor + "-" + id);
    }

    public static void open(int floor, int id) {
        TimableOutput.println("OPEN-" + floor + "-" + id);
    }

    public static void close(int floor, int id) {
        TimableOutput.println("CLOSE-" + floor + "-" + id);
    }

    public static void in(int personId, int floor, int elevatorId) {
        TimableOutput.println("IN-" + personId + "-" + floor + "-" + elevatorId);
    }

    public static void out(int personId, int floor, int elevatorId) {
        TimableOutput.println("OUT-" + personId + "-" + floor + "-" + elevatorId);
    }
}
