import java.math.BigInteger;
import java.util.ArrayList;

public class Expr implements Factor {
    private final ArrayList<Term> terms = new ArrayList<>();

    private final ArrayList<Token> ops = new ArrayList<>();

    private final BigInteger exp;

    public Expr(ArrayList<Term> terms, ArrayList<Token> ops, String exp) {
        this.terms.addAll(terms);
        this.ops.addAll(ops);
        this.exp = new BigInteger(exp);
    }

    @Override
    public Polynomial buildPoly() {
        if (exp.equals(new BigInteger("0"))) {
            Polynomial polynomial = new Polynomial();
            polynomial.addMonomial(new BigInteger("0"),new BigInteger("1"));
            return polynomial;
        }
        else {
            Polynomial polynomial = terms.get(0).buildPoly();
            for (int i = 1; i < terms.size(); i++) {
                if (ops.get(i - 1).getType().equals(Token.Type.ADD)) {
                    polynomial = polynomial.addPolynomial(terms.get(i).buildPoly());
                }
                else if (ops.get(i - 1).getType().equals(Token.Type.SUB)) {
                    polynomial = polynomial.subPolynomial(terms.get(i).buildPoly());
                }
            }
            Polynomial polynomial1 = polynomial;
            for (int i = 0; i < exp.intValue() - 1; i++) {
                polynomial = polynomial.mulPolynomial(polynomial1);
            }
            return polynomial;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(terms.get(0).toString());
        for (int i = 0; i < ops.size(); i++) {
            sb.append(ops.get(i).toString());
            sb.append(terms.get(i + 1).toString());
        }
        sb.append(")^");
        sb.append(exp);
        return sb.toString();
    }
}
