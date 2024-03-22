import java.util.HashMap;

public class DxFac implements Factor {
    private Expr expr;

    public DxFac(Expr expr) {
        this.expr = expr;
    }

    @Override
    public Factor substitute(HashMap<String, Factor> inputs) {
        return new DxFac(this.expr.substitute(inputs));
    }

    @Override
    public Polynomial toPoly() {
        Polynomial exprPoly = expr.toPoly();
        return exprPoly.dx();
    }
}
