import java.math.BigInteger;

public class Num implements Factor {
    private final BigInteger value;

    public Num(String num) {
        value = new BigInteger(num);
    }

    @Override
    public Polynomial count() {
        Polynomial result = new Polynomial();
        result.setCoefficient(0, value);
        return result;
    }
}
