import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;

public class LegalTerm {
    private BigInteger num;
    private HashMap<String, Integer> variables;

    public LegalTerm(BigInteger num, HashMap<String, Integer> variables) {
        this.num = num;
        this.variables = new HashMap<>();
        if (!num.equals(new BigInteger("0"))) {
            if (!variables.isEmpty()) {
                for (String string : variables.keySet()) {
                    this.variables.put(string, variables.get(string));
                }
            }
        }
    }

    public LegalTerm multiply(LegalTerm term) {
        HashMap<String, Integer> hashMap = new HashMap<>();
        if (!this.num.multiply(term.num).equals(new BigInteger("0"))) {
            if (!this.variables.isEmpty()) {
                for (String string : this.variables.keySet()) {
                    hashMap.put(string, this.variables.get(string));
                }
            }
            if (!term.variables.isEmpty()) {
                for (String string : term.variables.keySet()) {
                    if (hashMap.containsKey(string)) {
                        int newExpo = hashMap.get(string) + term.variables.get(string);
                        hashMap.replace(string, newExpo);
                    } else {
                        hashMap.put(string, term.variables.get(string));
                    }
                }
            }
        }
        return new LegalTerm(this.num.multiply(term.num), hashMap);
    }

    public void addSame(BigInteger num) {
        this.num = this.num.add(num);
    }

    public BigInteger getNum() {
        return num;
    }

    public String type() {
        if (variables.isEmpty()) {
            return "1";
        }
        else {
            StringBuilder sb = new StringBuilder();
            Iterator<String> iter = variables.keySet().iterator();
            String v = iter.next();
            sb.append(v);
            if (variables.get(v) != 1) {
                sb.append(" ^ ");
                sb.append(variables.get(v));
            }
            if (iter.hasNext()) {
                sb.append(" * ");
                v = iter.next();
                sb.append(v);
                if (variables.get(v) != 1) {
                    sb.append(" ^ ");
                    sb.append(variables.get(v));
                }
                while (iter.hasNext()) {
                    sb.append(" * ");
                    v = iter.next();
                    sb.append(v);
                    if (variables.get(v) != 1) {
                        sb.append(" ^ ");
                        sb.append(variables.get(v));
                    }
                }
            }
            return sb.toString();
        }
    }

    public LegalExpr upgrade() {
        LegalExpr expr = new LegalExpr();
        expr.addTerm(this);
        return expr;
    }
}
