import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Map.Entry;

public class Expr extends Factor {
    private final ArrayList<Term> terms = new ArrayList<>();
    private TreeMap<Integer, BigInteger> varMap = new TreeMap<>();

    public Expr() {
        ;
    }

    public Expr(Number number) {
        this.varMap.put(0, number.toBigInteger()); 
    }

    public Expr(Variable var) {
        this.varMap.put(var.getPower(), var.getCoefficient());
    }

    public void addTerm(Term term) {
        this.terms.add(term);
    }

    public Expr unfold() {
        Expr expr = new Expr();
        if (this.getPower() == 0) {
            Number num = new Number(new BigInteger("1"));
            expr = new Expr(num);
            expr.reverse(this.getMinus());
            return expr;
        } else {
            int len = terms.size();
            expr = terms.get(0).unfold();
            for (int i = 1; i < len; i++) {
                expr.merge(terms.get(i).unfold());
            }

            expr.calPower(this.getPower());
            expr.reverse(this.getMinus());
            
            return expr;
        }
    }

    public void merge(Expr expr) {
        Iterator<Entry<Integer, BigInteger>> expr2 = expr.varMap.entrySet().iterator();
        while (expr2.hasNext()) {
            Entry<Integer, BigInteger> entry2 = expr2.next();
            if (this.varMap.containsKey(entry2.getKey())) {
                BigInteger value = this.varMap.get(entry2.getKey()).add(entry2.getValue());
                this.varMap.put(entry2.getKey(), value);
            } else {
                this.varMap.put(entry2.getKey(), entry2.getValue());
            }
        }
    }

    public void merge() {
        int len = terms.size();
        for (int i = 0; i < len; i++) {
            Term term = terms.get(i);
            if (!varMap.containsKey(term.getPower())) {
                varMap.put(term.getPower(), term.getCoefficient());
            } else {
                if (term.getMinus() == 1) {
                    BigInteger temp = varMap.get(term.getPower());
                    temp = temp.add(term.getCoefficient());
                } else if (term.getMinus() == -1) {
                    BigInteger temp = varMap.get(term.getPower());
                    temp = temp.subtract(term.getCoefficient());
                }
            }
        }
    }

    public String toString() {
        Iterator<Entry<Integer, BigInteger>> iter;
        int flag = 1;
        iter = this.varMap.entrySet().iterator();
        StringBuilder sb = new StringBuilder();
        int firstKey = -1;
        while (iter.hasNext()) {
            Entry<Integer, BigInteger> entry = iter.next();
            if (entry.getValue().compareTo(new BigInteger("0")) > 0) {
                firstKey = entry.getKey();
                sb.append(entryToString(entry));
                flag = 0;
                break;
            }
        }

        iter = this.varMap.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<Integer, BigInteger> entry = iter.next();
            if (entry.getKey() != firstKey) {
                if (flag == 1) {
                    String str = entryToString(entry);
                    if (!str.isEmpty()) {
                        flag = 0;
                    }
                    sb.append(str);
                } else {
                    if (entry.getValue().compareTo(new BigInteger("0")) > 0) {
                        sb.append("+");
                    }
                    sb.append(entryToString(entry));
                }
            }
        }

        if (flag == 1) {
            sb.append("0");
        }

        return sb.toString();
    }

    public String entryToString(Entry<Integer, BigInteger> entry) {
        if (entry.getValue().compareTo(new BigInteger("0")) == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        if (entry.getKey() == 0) {
            if (!entry.getValue().toString().equals("0")) {
                sb.append(entry.getValue().toString());
            }
            return sb.toString();
        }

        if (entry.getValue().abs().compareTo(new BigInteger("1")) != 0) {
            sb.append(entry.getValue().toString());
            sb.append("*");
        } else if (entry.getValue().compareTo(new BigInteger("-1")) == 0) {
            sb.append("-");
        }
        
        sb.append("x");
        if (entry.getKey() != 1) {
            sb.append("^");
            sb.append(entry.getKey().toString());
        }

        return sb.toString();
    }

    public Expr multExpr(Expr expr) {
        TreeMap<Integer, BigInteger> ans = new TreeMap<>();
        Iterator<Entry<Integer, BigInteger>> expr1 = this.varMap.entrySet().iterator();
        Iterator<Entry<Integer, BigInteger>> expr2 = expr.varMap.entrySet().iterator();
        while (expr1.hasNext()) {
            Entry<Integer, BigInteger> entry1 = expr1.next();
            expr2 = expr.varMap.entrySet().iterator();
            while (expr2.hasNext()) {
                Entry<Integer, BigInteger> entry2 = expr2.next();
                int key = entry1.getKey().intValue() + entry2.getKey().intValue();
                BigInteger value = entry1.getValue().multiply(entry2.getValue());
                if (ans.containsKey(key)) {
                    value = ans.get(key).add(value);
                    ans.put(key, value);
                } else {
                    ans.put(key, value);
                }
            }
        }
        this.varMap = ans;
        return this;
    }

    public Expr calPower(int power) {
        if (power == 1) {
            return this;
        }

        TreeMap<Integer, BigInteger> expr = new TreeMap<>();
        Iterator<Entry<Integer, BigInteger>> expr1 = this.varMap.entrySet().iterator();
        while (expr1.hasNext()) {
            Entry<Integer, BigInteger> entry = expr1.next();
            BigInteger value = new BigInteger(entry.getValue().toString());
            expr.put(entry.getKey(), value);
        }

        TreeMap<Integer, BigInteger> ans;
        Iterator<Entry<Integer, BigInteger>> expr2;
        for (int i = 1; i < power; i++) {
            expr1 = this.varMap.entrySet().iterator();
            expr2 = expr.entrySet().iterator();
            ans = new TreeMap<>();
            while (expr1.hasNext()) {
                Entry<Integer, BigInteger> entry1 = expr1.next();
                expr2 = expr.entrySet().iterator();
                while (expr2.hasNext()) {
                    Entry<Integer, BigInteger> entry2 = expr2.next();
                    int key = entry1.getKey().intValue() + entry2.getKey().intValue();
                    BigInteger value = entry1.getValue().multiply(entry2.getValue());
                    if (ans.containsKey(key)) {
                        value = ans.get(key).add(value);
                        ans.put(key, value);
                    } else {
                        ans.put(key, value);
                    }
                }
            }
            this.varMap = ans;
        }
        return this;
    }

    public void reverse(int minus) {
        if (minus > 0) {
            return;
        }

        Iterator<Entry<Integer, BigInteger>> iter = this.varMap.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<Integer, BigInteger> entry = iter.next();
            BigInteger value = entry.getValue().multiply(new BigInteger("-1"));
            varMap.put(entry.getKey(), value);
        }
    }
}
