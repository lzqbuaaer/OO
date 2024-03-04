import java.math.BigInteger;
import java.util.HashSet;
import java.util.Iterator;

public class Term {
    private HashSet<Factor> factors;

    public Term() {
        this.factors = new HashSet<>();
    }

    public void AddFactor(Factor factor) {
        this.factors.add(factor);
    }

    public HashSet<Factor> GetAllNumber() {
        HashSet<Factor> allnumber = new HashSet<>();
        Iterator<Factor> iter = factors.iterator();
        while (iter.hasNext()) {
            Factor factor = iter.next();
            if (factor instanceof Number) {
                allnumber.add(factor);
            }
        }
        return allnumber;
    }

    public HashSet<Factor> GetAllVariable() {
        HashSet<Factor> allvariable = new HashSet<>();
        Iterator<Factor> iter = factors.iterator();
        while (iter.hasNext()) {
            Factor factor = iter.next();
            if (factor instanceof Variable) {
                allvariable.add(factor);
            }
        }
        return allvariable;
    }

    public HashSet<Factor> GetFactors() {
        return this.factors;
    }

    public Boolean HaveExpression() {
        Iterator<Factor> iter = factors.iterator();
        while (iter.hasNext()) {
            if (iter.next() instanceof Expression) {
                return true;
            }
        }
        return false;
    }

    public Number BuildAllNumber() {
        BigInteger numbuilt = BigInteger.valueOf(1);
        Number numberbulit = new Number(numbuilt);
        HashSet<Factor> allnumber = this.GetAllNumber();
        Iterator<Factor> iter = allnumber.iterator();
        while (iter.hasNext()) {
            numberbulit = numberbulit.MultWithNumber((Number) iter.next());
        }
        return numberbulit;
    }

    public Variable BuildAllVariable() {
        Variable variablebuilt = new Variable("x");
        variablebuilt.SetIndex(0);
        HashSet<Factor> allvariable = this.GetAllVariable();
        Iterator<Factor> iter = allvariable.iterator();
        while (iter.hasNext()) {
            variablebuilt = variablebuilt.MultWithVariable((Variable) iter.next());
        }
        return variablebuilt;
    }

    public Term MultWithTerm(Term term) {

        Term result = new Term();

        Iterator<Factor> iter1 = this.factors.iterator();

        while (iter1.hasNext()) {
            Factor a = iter1.next();
            if (result.factors.contains(a)) {
                result.AddFactor(a.Copy());
            } else {
                result.AddFactor(a);
            }
        }

        Iterator<Factor> iter2 = term.factors.iterator();

        while (iter2.hasNext()) {
            Factor b = iter2.next();
            if (result.factors.contains(b)) {
                result.AddFactor(b.Copy());
            } else {
                result.AddFactor(b);
            }
        }
        return result;
    }

    public void AddWithTerm(Term term) {
        Iterator<Factor> iter1 = this.factors.iterator();
        while (iter1.hasNext()) {
            Factor a = iter1.next();
            if (a instanceof Number) {
                Iterator<Factor> iter2 = term.factors.iterator();
                while (iter2.hasNext()) {
                    Factor b = iter2.next();
                    if (b instanceof Number) {
                        ((Number)a).AddWithNumber((Number) b);
                    }
                }
            }
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.BuildAllNumber().GetNum().compareTo(new BigInteger("0")) == 0) {
            return sb.toString();
        }
        if (this.BuildAllNumber().GetNum().compareTo(new BigInteger("0")) == 1) {
            sb.append("+");
            if (this.BuildAllNumber().GetNum().compareTo(new BigInteger("1")) == 0) {
                if (this.BuildAllVariable().toString().equals("")) {
                    sb.append("1");
                } else {
                    sb.append(this.BuildAllVariable().toString());
                }
            }
            else {
                sb.append(this.BuildAllNumber().toString());
                if (!this.BuildAllVariable().toString().equals("")) {
                    sb.append("*");
                    sb.append(BuildAllVariable().toString());
                }
            }
        }
        else {
            if (this.BuildAllNumber().GetNum().compareTo(new BigInteger("-1")) == 0) {
                sb.append("-");
                if (this.BuildAllVariable().toString().equals("")) {
                    sb.append("1");
                } else {
                    sb.append(this.BuildAllVariable().toString());
                }
            }
            else {
                sb.append(this.BuildAllNumber().toString());
                if (!this.BuildAllVariable().toString().equals("")) {
                    sb.append("*");
                    sb.append(BuildAllVariable().toString());
                }
            }
        }
        return sb.toString();
    }

    public Term Copy() {
        Term term = new Term();
        Iterator<Factor> iter = this.factors.iterator();
        while (iter.hasNext()) {
            term.AddFactor(iter.next().Copy());
        }
        return term;
    }
}
