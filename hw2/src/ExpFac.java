import java.math.BigInteger;
import java.util.HashMap;

public class ExpFac implements Factor {
    private final Expr expr;
    private final BigInteger index;

    public ExpFac(BigInteger index, Expr expr) {
        this.index = index;
        this.expr = expr;
    }

    @Override
    public ExpFac substitute(HashMap<String, Factor> inputs) {
        BigInteger indexCopy = new BigInteger(String.valueOf(index));
        Expr exprCopy = expr.substitute(inputs);
        return new ExpFac(indexCopy, exprCopy);
    }

    @Override
    public Polynomial toPoly() {
        Polynomial exprPoly = expr.toPoly();
        Polynomial poly = new Polynomial();
        poly.addMono(new Monomial(BigInteger.ONE, BigInteger.ZERO, new Polynomial()));
        for (BigInteger i = BigInteger.ZERO; i.compareTo(this.index) < 0;
             i = i.add(BigInteger.ONE)) {
            poly = poly.mulPoly(exprPoly);
        }
        return poly;
    }
}
