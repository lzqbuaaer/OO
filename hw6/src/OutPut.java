import com.oocourse.elevator2.TimableOutput;

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

    public static void resetBegin(int elevatorId) {
        TimableOutput.println("RESET_BEGIN-" + elevatorId);
    }

    public static void resetEnd(int elevatorId) {
        TimableOutput.println("RESET_END-" + elevatorId);
    }

    public static void receive(int personId, int elevatorId) {
        TimableOutput.println("RECEIVE-" + personId + "-" + elevatorId);
    }
}
