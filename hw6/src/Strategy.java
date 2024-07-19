public class Strategy {
    public static Action getAction(Elevator elevator, RequestTable reqTab) {
        if (elevator.needOut() || elevator.canIn()) {
            return Action.OPEN;
        } else if (elevator.needReset()) {
            return Action.RESET;
        } else if (!elevator.isEmpty()) {
            return Action.MOVE;
        } else if (reqTab.isEmpty()) {
            if (reqTab.isEnd()) {
                return Action.OVER;
            } else {
                return Action.WAIT;
            }
        } else {
            if (reqTab.towardsHasPerson(elevator.getFloor(), elevator.getTowards())) {
                return Action.MOVE;
            } else {
                return Action.REVERSE;
            }
        }
    }

    public static Action getShadowAction(ShadowElevator elevator, RequestTable reqTab) {
        if (elevator.needOut() || elevator.canIn()) {
            return Action.OPEN;
        } else if (!elevator.isEmpty()) {
            return Action.MOVE;
        } else if (reqTab.isEmpty()) {
            return Action.OVER;
        } else {
            if (reqTab.towardsHasPerson(elevator.getFloor(), elevator.getTowards())) {
                return Action.MOVE;
            } else {
                return Action.REVERSE;
            }
        }
    }
}