import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class Expr implements Factor {
    private HashMap<Term, BigInteger> poly;

    public Expr() {
        this.poly = new HashMap<>();
    }

    public Expr(BigInteger a) {
        this.poly = new HashMap<>();
        poly.put(new Term(), a);
    }

    public Expr(Integer b) {
        this.poly = new HashMap<>();
        BigInteger one = new BigInteger("1");
        poly.put(new Term(b), one);
    }

    public void addTerm(Term term, BigInteger big) {
        int flag = 1;
        for (Term teRm : poly.keySet()) {
            if (teRm.equalTerm(term)) {
                BigInteger cur = poly.get(teRm).add(big);
                BigInteger old = poly.get(teRm);
                flag = 0;
                poly.replace(teRm, old, cur);
                break;
            }
        }
        if (flag == 1) {
            poly.put(term, big);
        }
    }

    public void subTerm(Term term, BigInteger big) {
        int flag = 1;
        for (Term teRm : poly.keySet()) {
            if (teRm.equalTerm(term)) {
                BigInteger cur = poly.get(teRm).subtract(big);
                BigInteger old = poly.get(teRm);
                flag = 0;
                poly.replace(teRm, old, cur);
                break;
            }
        }
        BigInteger neg = new BigInteger("-1");
        if (flag == 1) {
            poly.put(term, big.multiply(neg));
        }
    }

    public HashMap getPoly() {
        return poly;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        BigInteger zero = new BigInteger("0");
        for (HashMap.Entry<Term, BigInteger> enTry : poly.entrySet()) {
            int com = zero.compareTo(enTry.getValue());
            if (com < 0) {
                sb.append(print(enTry));
            }
        }
        for (HashMap.Entry<Term, BigInteger> entry : poly.entrySet()) {
            int com = zero.compareTo(entry.getValue());
            if (com > 0) {
                sb.append(print(entry));
            }
        }
        if (sb.toString().isEmpty()) {
            return "0";
        } else {
            if (sb.toString().charAt(0) == '+') {
                return sb.substring(1);
            } else {
                return sb.toString().replace("+0", "");
            }
        }
    }

    public String print(Map.Entry<Term, BigInteger> entry) {
        BigInteger zero = new BigInteger("0");
        BigInteger one = new BigInteger("1");
        BigInteger neg = new BigInteger("-1");
        StringBuilder sb = new StringBuilder();

        int com = zero.compareTo(entry.getValue());
        int sos = one.compareTo(entry.getValue());
        int kpi = neg.compareTo(entry.getValue());
        if (com < 0) {
            sb.append("+");
        }
        if (entry.getKey().getPow() == 0) {
            sb.append(entry.getValue());
        } else {
            if (kpi == 0) {
                sb.append('-');
            } else {
                if (sos != 0) {
                    sb.append(entry.getValue());
                    sb.append("*");
                }
            }
            sb.append("x");
            if (entry.getKey().getPow() != 1) {
                sb.append("^");
                sb.append(entry.getKey().getPow());
            }
        }
        return sb.toString();
    }
}

