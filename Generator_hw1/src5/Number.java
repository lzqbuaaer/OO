import java.math.BigInteger;
import java.util.HashMap;

public class Number implements Factor {
    private final BigInteger num;

    public Number(BigInteger num) {
        this.num = num;
    }

    public BigInteger value() {
        return  num;
    }

    @Override
    public Number negate() {
        return new Number(num.negate());
    }

    @Override
    public Number powered(int exponent) {
        return new Number(num.pow(exponent));
    }

    @Override
    public LegalExpr turnLegal() {
        return turnFormalTerm().upgrade();
    }

    public LegalTerm turnFormalTerm() {
        HashMap<String, Integer> hashMap = new HashMap<>();
        return new LegalTerm(this.num, hashMap);
    }
}
