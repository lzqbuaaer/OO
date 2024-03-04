import java.math.BigInteger;

public class Pow implements Factor {
    private final int index;

    public Pow(int index) {
        this.index = index;
    }

    @Override
    public Polynomial count() {
        Polynomial result = new Polynomial();
        result.setCoefficient(index, BigInteger.valueOf(1));
        return result;
    }
}
