import java.math.BigInteger;
import java.util.HashMap;

public class Num implements Factor {
    private final BigInteger value;

    public Num(String num) {
        value = new BigInteger(num);
    }

    @Override
    public Num substitute(HashMap<String, Factor> inputs) {
        return new Num(String.valueOf(value));
    }

    public Polynomial toPoly() {
        Monomial mono = new Monomial(value, BigInteger.ZERO, new Polynomial());
        Polynomial poly = new Polynomial();
        poly.addMono(mono);
        return poly;
    }
}
