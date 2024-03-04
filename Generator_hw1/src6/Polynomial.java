import java.math.BigInteger;
import java.util.HashMap;

public class Polynomial {
    private HashMap<BigInteger, BigInteger> polynomial;

    public Polynomial() {
        polynomial = new HashMap<>();
    }

    public void addMonomial(BigInteger exp, BigInteger coefficient) {
        polynomial.put(exp, coefficient);
    }

    public Polynomial addPolynomial(Polynomial polynomial) {
        Polynomial poly = new Polynomial();
        for (BigInteger exp : this.getPolynomial().keySet()) {
            BigInteger coef1 = this.getPolynomial().get(exp);
            if (polynomial.getPolynomial().containsKey(exp)) {
                BigInteger coef2 = polynomial.getPolynomial().get(exp).add(coef1);
                poly.addMonomial(exp, coef2);
            }
            else {
                poly.addMonomial(exp, coef1);
            }
        }
        for (BigInteger exp : polynomial.getPolynomial().keySet()) {
            if (!poly.getPolynomial().containsKey(exp)) {
                BigInteger coef2 = polynomial.getPolynomial().get(exp);
                poly.addMonomial(exp, coef2);
            }
        }
        return poly;
    }

    public Polynomial subPolynomial(Polynomial polynomial2) {
        Polynomial poly = new Polynomial();
        for (BigInteger exp : this.getPolynomial().keySet()) {
            BigInteger coef1 = this.getPolynomial().get(exp);
            if (polynomial2 != null && polynomial2.getPolynomial().containsKey(exp)) {
                BigInteger coef2 = coef1.subtract(polynomial2.getPolynomial().get(exp));
                poly.addMonomial(exp, coef2);
            }
            else {
                poly.addMonomial(exp, coef1);
            }
        }
        if (polynomial2 != null) {
            for (BigInteger exp : polynomial2.getPolynomial().keySet()) {
                if (!poly.getPolynomial().containsKey(exp)) {
                    BigInteger coef2 = polynomial2.getPolynomial().get(exp);
                    poly.addMonomial(exp, coef2.negate());
                }
            }
        }
        return poly;
    }

    public Polynomial mulPolynomial(Polynomial polynomial2) {
        Polynomial poly = new Polynomial();
        for (BigInteger exp1 : this.getPolynomial().keySet()) {
            BigInteger coef1 = this.getPolynomial().get(exp1);
            for (BigInteger exp2 : polynomial2.getPolynomial().keySet()) {
                BigInteger coef2 = polynomial2.getPolynomial().get(exp2);
                BigInteger exp = exp1.add(exp2);
                if (poly.getPolynomial().containsKey(exp)) {
                    BigInteger coef = poly.getPolynomial().get(exp);
                    poly.addMonomial(exp, coef.add(coef1.multiply(coef2)));
                }
                else {
                    poly.addMonomial(exp, coef1.multiply(coef2));
                }
            }
        }
        return poly;
    }

    public HashMap<BigInteger, BigInteger> getPolynomial() {
        return polynomial;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int flag  = 0;
        for (BigInteger exp : this.polynomial.keySet()) {
            BigInteger  coef = this.polynomial.get(exp);
            if (coef.compareTo(new BigInteger("-1")) == 0) { // 只加-
                sb.append("-");
            }
            else if (coef.compareTo(new BigInteger("1")) == 0) { // 只加+
                if (flag != 0) {
                    sb.append("+");
                }
            }
            else if (coef.compareTo(new BigInteger("0")) > 0) {
                if (flag != 0) { //第一项不用加+
                    sb.append("+");
                }
                sb.append(coef);
                sb.append("*");
            }
            else if (coef.compareTo(new BigInteger("0")) < 0) {
                sb.append(coef);
                sb.append("*");
            }
            else {
                continue;
            }
            if (exp.compareTo(new BigInteger("0")) > 0) { //指数不是0
                sb.append("x");
                if (exp.compareTo(new BigInteger("1")) > 0) { //指数不为1
                    sb.append("^");
                    sb.append(exp);
                }
            }
            else { //常数 指数是0
                if (coef.compareTo(new BigInteger("-1")) != 0
                        && coef.compareTo(new BigInteger("1")) != 0) {
                    if (sb.length() > 0) {
                        sb.deleteCharAt(sb.length() - 1); //删*
                    }
                }
                else {
                    sb.append("1"); //系数是1或-1，由于前文系数省略，补上1
                }
            }
            flag = 1; //后续非第一项
        }
        if (sb.length() == 0) { //0
            return "0";
        }
        if (sb.charAt(0) == '-') {
            for (int i = 0; i < sb.length(); i++) {
                if (sb.charAt(i) == '+') {
                    return sb.substring(i + 1) + sb.substring(0, i);
                }
            }
        }
        return sb.toString();
    }
}
