import java.util.ArrayList;
import java.util.HashMap;

public class FuncFac implements Factor {
    private final HashMap<String, Factor> inputs;
    private final Function function;
    private final Expr expr;

    public FuncFac(Function function, ArrayList<Factor> factors) {
        this.function = function;
        this.inputs = new HashMap<>();
        ArrayList<String> parameters = function.getParameters();
        for (int i = 0; i < parameters.size(); i++) {
            this.inputs.put(parameters.get(i), factors.get(i));
        }
        this.expr = function.getExpr().substitute(this.inputs);
    }

    @Override
    public FuncFac substitute(HashMap<String, Factor> inputs) {
        return null;
    }

    @Override
    public Polynomial toPoly() {
        return this.expr.toPoly();
    }
}
