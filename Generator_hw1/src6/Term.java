import java.util.ArrayList;

public class Term {
    private final ArrayList<Factor> factors = new ArrayList<>();

    public Term(ArrayList<Factor> factors) {
        this.factors.addAll(factors);
    }

    public Polynomial buildPoly() {
        Polynomial polynomial = factors.get(0).buildPoly();
        for (int i = 1; i < factors.size(); i++) {
            polynomial = polynomial.mulPolynomial(factors.get(i).buildPoly());
        }
        return polynomial;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(factors.get(0).toString());
        for (int i = 1; i < factors.size(); i++) {
            sb.append("*");
            sb.append(factors.get(i).toString());
        }
        return sb.toString();
    }
}
