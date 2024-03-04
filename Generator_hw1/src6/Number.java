import java.math.BigInteger;

public class Number implements Factor {
    private final BigInteger num;

    public Number(String num) {
        this.num = new BigInteger(num);
    }

    @Override
    public String toString() {
        return String.valueOf(num);
    }

    public Polynomial buildPoly() {
        Polynomial polynomial = new Polynomial();
        polynomial.addMonomial(new BigInteger("0"), num);
        return polynomial;
    }
}
