import java.math.BigInteger;
import java.util.ArrayList;

public class Expr implements Factor {
    private final ArrayList<Term> terms;

    public Expr() {
        this.terms = new ArrayList<>();
    }

    public void addTerm(Term term) {
        this.terms.add(term);
    }

    @Override
    public Factor negate() {
        for (Term term : terms) {
            term.negate();
        }
        return this;
    }

    @Override
    public Factor powered(int exponent) {
        if (exponent == 0) {
            return new Number(new BigInteger("1"));
        }
        else if (exponent == 1) {
            return this;
        }
        else {
            Term power = new Term();
            for (int i = 0; i < exponent; i++) {
                power.addFactor(this);
            }
            return power;
        }
    }

    @Override
    public LegalExpr turnLegal() {
        LegalExpr sum = new LegalExpr();
        for (Term term : terms) {
            sum.addExpr(term.turnLegal());
        }
        return sum;
    }
}
