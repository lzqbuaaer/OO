import java.math.BigInteger;

public class Var implements Factor {
    private final BigInteger pow;

    private final String argument;

    public Var(String argument,String power) {
        this.pow = new BigInteger(power);
        this.argument = String.valueOf(argument);
    }

    public Polynomial buildPoly() {
        Polynomial polynomial = new Polynomial();
        polynomial.addMonomial(pow, new BigInteger("1"));
        return polynomial;
    }

    public String toString() {
        return "x^" + pow;
    }
}
