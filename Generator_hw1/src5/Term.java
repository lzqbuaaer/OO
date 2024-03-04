import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class Term implements Factor {
    private final ArrayList<Factor> factors;

    public Term() {
        this.factors = new ArrayList<>();
    }

    public void addFactor(Factor factor) {
        this.factors.add(factor);
    }

    @Override
    public Term negate() {
        this.addFactor(new Number(new BigInteger("-1")));
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
            for (Factor factor : factors) {
                factor.powered(exponent);
            }
            return this;
        }
    }

    @Override
    public LegalExpr turnLegal() {
        LegalExpr product = new LegalExpr();
        LegalTerm one = new LegalTerm(new BigInteger("1"), new HashMap<>());
        product.addTerm(one);
        for (Factor factor : factors) {
            product = product.multiply(factor.turnLegal());
        }
        return product;
    }
}
