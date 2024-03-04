import java.math.BigInteger;

public class ExpFac implements Factor {
    private final Expr expr;
    private final int index;

    public ExpFac(int index, Expr expr) {
        this.index = index;
        this.expr = expr;
    }

    @Override
    public Polynomial count() {
        if (index == 0) {
            Polynomial result = new Polynomial();
            result.setCoefficient(0, BigInteger.valueOf(1));
            return result;
        }
        Polynomial result = expr.count();
        for (int i = 0; i < index - 1; i++) {
            result = result.mul(expr.count());
        }
        return result;
    }
}
