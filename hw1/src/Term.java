import java.math.BigInteger;
import java.util.ArrayList;

public class Term {
    private final int mark;
    private final ArrayList<Factor> factors = new ArrayList<>();

    public Term(int mark, ArrayList<Factor> factors) {
        this.factors.addAll(factors);
        this.mark = mark;
    }

    public Polynomial count() {
        Polynomial result = new Polynomial();
        result.setCoefficient(0, BigInteger.valueOf(mark));
        for (Factor factor : factors) {
            result = result.mul(factor.count());
        }
        return result;
    }
}
