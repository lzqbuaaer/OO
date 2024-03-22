import java.util.ArrayList;
import java.util.HashMap;

public class FuncFac implements Factor {
    private final HashMap<String, Factor> inputs;
    private final Function function;
    private Expr expr;

    public FuncFac(Function function, ArrayList<Factor> factors) {
        this.function = function;
        this.inputs = new HashMap<>();
        ArrayList<String> parameters = function.getParameters();
        for (int i = 0; i < parameters.size(); i++) {
            this.inputs.put(parameters.get(i), factors.get(i));
        }
        this.expr = function.getExpr().substitute(this.inputs);
    }

    public FuncFac(HashMap<String, Factor> inputs, Function function, Expr expr) {
        this.expr = expr;
        this.inputs = inputs;
        this.function = function;
    }

    @Override
    public FuncFac substitute(HashMap<String, Factor> inputs) {
        return new FuncFac(this.inputs, this.function, this.expr.substitute(inputs));
    }

    @Override
    public Polynomial toPoly() {
        return this.expr.toPoly();
    }
}
