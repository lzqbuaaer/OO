import java.math.BigInteger;

public class Number extends Factor {
    public Number(BigInteger num) {
        this.setCoefficient(num);
    }

    public BigInteger toBigInteger() {
        return this.getCoefficient();
    }

    public Expr unfold() {
        Expr newExpr = new Expr(this);
        return newExpr;
    }
}
