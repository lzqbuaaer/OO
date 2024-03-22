import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class Term {
    private final int mark;
    private final ArrayList<Factor> factors = new ArrayList<>();

    public Term(int mark, ArrayList<Factor> factors) {
        this.factors.addAll(factors);
        this.mark = mark;
    }

    public Term substitute(HashMap<String, Factor> inputs) {
        int markCopy = this.mark;
        ArrayList<Factor> factorsCopy = new ArrayList<>();
        for (Factor factor : this.factors) {
            factorsCopy.add(factor.substitute(inputs));
        }
        return new Term(markCopy, factorsCopy);
    }

    public Polynomial toPoly() {
        Polynomial poly = new Polynomial();
        Monomial init = new Monomial(new BigInteger(String.valueOf(mark)),
                BigInteger.ZERO, new Polynomial());
        poly.addMono(init);
        for (Factor factor : factors) {
            poly = poly.mulPoly(factor.toPoly());
        }
        return poly;
    }
}
