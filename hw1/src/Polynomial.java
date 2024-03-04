import java.math.BigInteger;
import java.util.HashMap;

public class Polynomial {
    private final HashMap<Integer, BigInteger> coefficients;

    public Polynomial() {
        coefficients = new HashMap<>();
    }

    public void setCoefficient(int exp, BigInteger coef) {
        if (coefficients.containsKey(exp)) {
            BigInteger value = coef.add(coefficients.get(exp));
            coefficients.put(exp, value);
        } else {
            coefficients.put(exp, coef);
        }
    }

    public HashMap<Integer, BigInteger> getCoef() {
        return this.coefficients;
    }

    public void add(Polynomial pol) {
        for (Integer exp : pol.getCoef().keySet()) {
            if (this.coefficients.containsKey(exp)) {
                BigInteger ans = this.coefficients.get(exp);
                ans = ans.add(pol.getCoef().get(exp));
                this.coefficients.put(exp, ans);
            } else {
                this.coefficients.put(exp, pol.getCoef().get(exp));
            }
        }
    }

    public void sub(Polynomial pol) {
        for (Integer exp : pol.getCoef().keySet()) {
            if (this.coefficients.containsKey(exp)) {
                BigInteger ans = this.coefficients.get(exp);
                ans = ans.subtract(pol.getCoef().get(exp));
                this.coefficients.put(exp, ans);
            } else {
                this.coefficients.put(exp, pol.getCoef().get(exp).multiply(BigInteger.valueOf(-1)));
            }
        }
    }

    public Polynomial mul(Polynomial pol) {
        Polynomial result = new Polynomial();
        for (Integer exp1 : this.coefficients.keySet()) {
            for (Integer exp2 : pol.getCoef().keySet()) {
                BigInteger value = coefficients.get(exp1);
                value = value.multiply(pol.getCoef().get(exp2));
                result.setCoefficient(exp1 + exp2, value);
            }
        }
        return result;
    }

    public void print() {
        StringBuilder ans = new StringBuilder();
        for (Integer i : this.coefficients.keySet()) {
            BigInteger value = coefficients.get(i);
            int cmp = value.compareTo(BigInteger.ZERO);
            if (cmp < 0) {
                if (i == 0) {
                    ans.append(value);
                } else if (i == 1) {
                    if (value.compareTo(BigInteger.valueOf(-1)) == 0) {
                        ans.append("-x");
                    } else {
                        ans.append(value);
                        ans.append("*x");
                    }
                } else {
                    if (value.compareTo(BigInteger.valueOf(-1)) == 0) {
                        ans.append("-x^");
                        ans.append(i);
                    } else {
                        ans.append(value);
                        ans.append("*x^");
                        ans.append(i);
                    }
                }
            } else if (cmp > 0) {
                if (ans.length() != 0 && ans.charAt(0) != '-' && ans.charAt(0) != '+') {
                    ans.insert(0, "+");
                }
                if (i == 0) {
                    ans.insert(0, value);
                } else if (i == 1) {
                    if (value.compareTo(BigInteger.ONE) == 0) {
                        ans.insert(0, "x");
                    } else {
                        ans.insert(0, value + "*x");
                    }
                } else {
                    if (value.compareTo(BigInteger.ONE) == 0) {
                        ans.insert(0, "x^" + i);
                    } else {
                        ans.insert(0, value + "*x^" + i);
                    }
                }
            }
        }
        if (ans.length() > 0) {
            System.out.println(ans);
        } else {
            System.out.println("0");
        }
    }
}
