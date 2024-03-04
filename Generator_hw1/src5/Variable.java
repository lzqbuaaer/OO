import java.math.BigInteger;
import java.util.HashMap;

public class Variable implements Factor {
    private final String variable;

    private BigInteger value;

    private int exponent;

    public Variable(String v) {
        this.variable = v;
        this.exponent = 1;
    }

    @Override
    public Factor negate() {
        Variable v = new Variable(this.variable);
        v.value = this.value;
        Term negative = new Term();
        negative.addFactor(new Number(new BigInteger("-1")));
        negative.addFactor(v);
        return negative;
    }

    @Override
    public Variable powered(int exponent) {
        Variable v = new Variable(this.variable);
        v.exponent = this.exponent * exponent;
        v.value = this.value;
        return v;
    }

    @Override
    public LegalExpr turnLegal() {
        return turnFormalTerm().upgrade();
    }

    public LegalTerm turnFormalTerm() {
        HashMap<String, Integer> hashMap = new HashMap<>();
        hashMap.put(variable,exponent);
        return new LegalTerm(new BigInteger("1"), hashMap);
    }
}
