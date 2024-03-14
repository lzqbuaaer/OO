import java.math.BigInteger;
import java.util.HashMap;

public class Exp implements Factor {
    private Factor factor;
    private BigInteger index;

    public Exp(BigInteger index, Factor factor) {
        this.factor = factor;
        this.index = index;
    }

    @Override
    public Exp substitute(HashMap<String, Factor> inputs) {
        BigInteger indexCopy = new BigInteger(String.valueOf(index));
        Factor factorCopy = factor.substitute(inputs);
        return new Exp(indexCopy, factorCopy);
    }

    @Override
    public Polynomial toPoly() {
        Polynomial exp = factor.toPoly();
        Monomial monoIndex = new Monomial(this.index, BigInteger.ZERO, new Polynomial());
        Polynomial in = new Polynomial();
        in.addMono(monoIndex);
        exp = exp.mulPoly(in);
        Monomial mono = new Monomial(BigInteger.ONE, BigInteger.ZERO, exp);
        Polynomial poly = new Polynomial();
        poly.addMono(mono);
        return poly;
    }
}
