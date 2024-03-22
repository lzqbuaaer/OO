import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class Pow implements Factor {
    private final BigInteger index;
    private final String variable;

    public Pow(BigInteger index, String variable) {
        this.index = index;
        this.variable = variable;
    }

    @Override
    public ExpFac substitute(HashMap<String, Factor> inputs) {
        Factor factor = inputs.get(this.variable);
        ArrayList<Factor> factors = new ArrayList<>();
        factors.add(factor);
        Term term = new Term(1, factors);
        ArrayList<Term> terms = new ArrayList<>();
        terms.add(term);
        ArrayList<Token> ops = new ArrayList<>();
        ops.add(new Token(Token.Type.ADD, "+"));
        Expr exprCopy = new Expr(terms, ops);
        return new ExpFac(new BigInteger(String.valueOf(index)), exprCopy);
    }

    @Override
    public Polynomial toPoly() {
        Monomial mono = new Monomial(BigInteger.ONE, index, new Polynomial());
        Polynomial poly = new Polynomial();
        poly.addMono(mono);
        return poly;
    }
}
