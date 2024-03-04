import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;

public class LegalExpr {
    private HashMap<String, LegalTerm> terms;

    public LegalExpr() {
        this.terms = new HashMap<>();
        HashMap<String, Integer> hashMap = new HashMap<>();
        LegalTerm zero = new LegalTerm(new BigInteger("0"), hashMap);
        terms.put("1", zero);
    }

    public void addExpr(LegalExpr expr) {
        for (String string : expr.terms.keySet()) {
            if (terms.containsKey(string)) {
                terms.get(string).addSame(expr.terms.get(string).getNum());
                if (terms.get(string).getNum().equals(new BigInteger("0")) && !string.equals("1")) {
                    terms.remove(string);
                }
            }
            else {
                terms.put(string, expr.terms.get(string));
            }
        }
    }

    public void addTerm(LegalTerm term) {
        if (terms.containsKey(term.type())) {
            terms.get(term.type()).addSame(term.getNum());
            if (terms.get(term.type()).getNum().equals(new BigInteger("0"))
                    && !term.type().equals("1")) {
                terms.remove(term.type());
            }
        }
        else {
            terms.put(term.type(), term);
        }
    }

    public LegalExpr multiply(LegalExpr multiplicand) {
        LegalExpr product = new LegalExpr();
        for (String s1 : terms.keySet()) {
            for (String s2 : multiplicand.terms.keySet()) {
                LegalTerm midProduct = terms.get(s1).multiply(multiplicand.terms.get(s2));
                if (!midProduct.getNum().equals(new BigInteger("0"))) {
                    product.addTerm(midProduct);
                }
            }
        }
        return product;
    }

    public String out() {
        if (terms.size() > 1 && terms.get("1").getNum().equals(new BigInteger("0"))) {
            terms.remove("1");
        }
        Iterator<String> iter = terms.keySet().iterator();
        StringBuilder sb = new StringBuilder();
        String v = iter.next();
        if (v.equals("1")) {
            sb.append(terms.get(v).getNum());
        }
        else {
            if (!terms.get(v).getNum().equals(new BigInteger("1"))) {
                if (terms.get(v).getNum().equals(new BigInteger("-1"))) {
                    sb.append("- ");
                }
                else {
                    sb.append(terms.get(v).getNum());
                    sb.append(" * ");
                }
            }
            sb.append(v);
        }
        while (iter.hasNext()) {
            v = iter.next();
            if (terms.get(v).getNum().compareTo(new BigInteger("0")) < 0) {
                sb.append(" - ");
                if (v.equals("1")) {
                    sb.append(terms.get(v).getNum().negate());
                }
                else {
                    if (!terms.get(v).getNum().equals(new BigInteger("-1"))) {
                        sb.append(terms.get(v).getNum().negate());
                        sb.append(" * ");
                    }
                    sb.append(v);
                }
            }
            else {
                sb.append(" + ");
                if (v.equals("1")) {
                    sb.append(terms.get(v).getNum());
                } else {
                    if (!terms.get(v).getNum().equals(new BigInteger("1"))) {
                        sb.append(terms.get(v).getNum());
                        sb.append(" * ");
                    }
                    sb.append(v);
                }
            }
        }
        return sb.toString();
    }
}
