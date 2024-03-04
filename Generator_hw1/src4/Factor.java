import java.math.BigInteger;

public class Factor {
    private int minus = 1;
    private int power = 1;
    private BigInteger coefficient = new BigInteger("1");

    public void setCoefficient(BigInteger coefficient) {
        this.coefficient = coefficient;
    }

    public void setMinus(int minus) {
        this.minus = minus;
        if (minus < 0) {
            coefficient = coefficient.multiply(new BigInteger("-1"));
        }
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getMinus() {
        return this.minus;
    }

    public int getPower() {
        return this.power;
    }

    public void changeMinus(int minus) {
        this.minus = minus * this.minus;
        if (minus < 0) {
            coefficient = coefficient.multiply(new BigInteger("-1"));
        }
    }

    public BigInteger getCoefficient() {
        return this.coefficient;
    }

    public Expr unfold() {
        return new Expr();
    }
}