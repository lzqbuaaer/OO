import com.oocourse.elevator3.TimableOutput;

public class OutPut {
    public static void arrive(int floor, int id, int position) {
        if (position == 0) {
            TimableOutput.println("ARRIVE-" + floor + "-" + id);
        } else if (position == 1) {
            TimableOutput.println("ARRIVE-" + floor + "-" + id + "-A");
        } else {
            TimableOutput.println("ARRIVE-" + floor + "-" + id + "-B");
        }
    }

    public static void open(int floor, int id, int position) {
        if (position == 0) {
            TimableOutput.println("OPEN-" + floor + "-" + id);
        } else if (position == 1) {
            TimableOutput.println("OPEN-" + floor + "-" + id + "-A");
        } else {
            TimableOutput.println("OPEN-" + floor + "-" + id + "-B");
        }
    }

    public static void close(int floor, int id, int position) {
        if (position == 0) {
            TimableOutput.println("CLOSE-" + floor + "-" + id);
        } else if (position == 1) {
            TimableOutput.println("CLOSE-" + floor + "-" + id + "-A");
        } else {
            TimableOutput.println("CLOSE-" + floor + "-" + id + "-B");
        }
    }

    public static void in(int personId, int floor, int elevatorId, int position) {
        if (position == 0) {
            TimableOutput.println("IN-" + personId + "-" + floor + "-" + elevatorId);
        } else if (position == 1) {
            TimableOutput.println("IN-" + personId + "-" + floor + "-" + elevatorId + "-A");
        } else {
            TimableOutput.println("IN-" + personId + "-" + floor + "-" + elevatorId + "-B");
        }
    }

    public static void out(int personId, int floor, int elevatorId, int position) {
        if (position == 0) {
            TimableOutput.println("OUT-" + personId + "-" + floor + "-" + elevatorId);
        } else if (position == 1) {
            TimableOutput.println("OUT-" + personId + "-" + floor + "-" + elevatorId + "-A");
        } else {
            TimableOutput.println("OUT-" + personId + "-" + floor + "-" + elevatorId + "-B");
        }
    }

    public static void resetBegin(int elevatorId) {
        TimableOutput.println("RESET_BEGIN-" + elevatorId);
    }

    public static void resetEnd(int elevatorId) {
        TimableOutput.println("RESET_END-" + elevatorId);
    }

    public static void receive(int personId, int elevatorId, int position) {
        if (position == 0) {
            TimableOutput.println("RECEIVE-" + personId + "-" + elevatorId);
        } else if (position == 1) {
            TimableOutput.println("RECEIVE-" + personId + "-" + elevatorId + "-A");
        } else {
            TimableOutput.println("RECEIVE-" + personId + "-" + elevatorId + "-B");
        }
    }
}
