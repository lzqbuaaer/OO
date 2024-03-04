import java.math.BigInteger;
import java.util.ArrayList;

public class Term {
    private final ArrayList<Factor> factors = new ArrayList<>();
    private int minus = 1;
    private int power;
    private BigInteger coefficient = new BigInteger("1");

    public Term() {
        ;
    }

    public Term(int power, BigInteger coefficient) {
        this.power = power;
        this.coefficient = coefficient;
    }

    public void addFactor(Factor factor) {
        this.factors.add(factor);
    }

    public void setMinus(int minus) {
        this.minus = minus;
    }

    public void changeMinus(int minus) {
        this.minus = minus * this.minus;
    }

    public Expr unfold() {
        Expr expr = new Expr();
        int len = factors.size();
        for (int i = 0; i < len; i++) {
            Factor factor = factors.get(i);
            if (i == 0) {
                expr = factor.unfold();
            } else {
                expr.multExpr(factor.unfold());
            }
        }
        expr.reverse(this.getMinus());
        return expr;
    } 

    public String toString() {
        StringBuilder sb = new StringBuilder();
        int len = factors.size();
        for (int i = 0; i < len; i++) {
            factors.get(i).changeMinus(this.minus);
            sb.append(factors.get(i).toString());
        }

        return sb.toString();
    }

    public int getPower() {
        return this.power;
    }

    public BigInteger getCoefficient() {
        return this.coefficient;
    }

    public int getMinus() {
        return this.minus;
    }
}
