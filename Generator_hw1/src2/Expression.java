import java.math.BigInteger;
import java.util.HashSet;
import java.util.Iterator;

public class Expression implements Factor {
    private HashSet<Term> terms;

    private int index;

    public Expression() {
        this.terms = new HashSet<>();
        this.index = 1;
    }

    public void SetIndex(int index) {
        this.index = index;
    }

    public int GetIndex() {
        return this.index;
    }

    public void AddTerm(Term term) {
        this.terms.add(term);
    }

    public HashSet<Term> GetTerms() {
        return this.terms;
    }

    public Expression MultWithExpression(Expression expression) {
        Expression result = new Expression();
        Iterator<Term> iter1 = this.terms.iterator();
        while (iter1.hasNext()) {
            Term a = iter1.next();
            Iterator<Term> iter2 = expression.terms.iterator();
            while (iter2.hasNext()) {
                result.AddTerm(a.MultWithTerm(iter2.next()));
            }
        }
        return result;
    }

    public void AddWithExpression(Expression expression) {
        Iterator<Term> iter = expression.terms.iterator();
        while (iter.hasNext()) {
            this.AddTerm(iter.next());
        }
    }

    public Expression CalculateIndex() {
        BigInteger one = BigInteger.valueOf(1);
        Number number = new Number(one);
        Term term = new Term();
        term.AddFactor(number);
        Expression result = new Expression();
        result.AddTerm(term);
        for (int i = 0;i < this.index;i++) {
            result = result.MultWithExpression(this);
        }
        return result;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<Term> iter = this.terms.iterator();
        Term first = new Term();
        while (iter.hasNext()) {
            first = iter.next();
            if (first.BuildAllNumber().GetNum().compareTo(BigInteger.valueOf(0)) == 1) {
                sb.append(first);
                break;
            }
        }
        Iterator<Term> iter2 = this.terms.iterator();
        if (sb.toString().equals("")) {
            while (iter2.hasNext()) {
                sb.append(iter2.next());
            }
        } else {
            while (iter2.hasNext()) {
                Term next = iter2.next();
                if (next != first) {
                    sb.append(next);
                }
            }
        }
        /*while (iter.hasNext()) {
            sb.append(iter.next());
        }*/
        return sb.toString();
    }

    @Override
    public Expression Copy() {
        Expression expression = new Expression();
        Iterator<Term> iter = this.terms.iterator();
        while (iter.hasNext()) {
            expression.AddTerm(iter.next().Copy());
        }
        return expression;
    }
}
