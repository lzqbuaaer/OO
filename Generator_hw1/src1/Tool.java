
import java.math.BigInteger;
import java.util.Map;

public class Tool {
    public Expr Add(Expr left, Expr right) {
        for (Object o : right.getPoly().entrySet()) {
            Map.Entry<Term, BigInteger> entry = (Map.Entry<Term, BigInteger>) o;
            left.addTerm(entry.getKey(), entry.getValue());
        }
        return left;
    }

    public Expr Sub(Expr left, Expr right) {
        for (Object o : right.getPoly().entrySet()) {
            Map.Entry<Term, BigInteger> entry = (Map.Entry<Term, BigInteger>) o;
            left.subTerm(entry.getKey(), entry.getValue());
        }
        return left;
    }

    public Term mulTerm(Term term1, Term term2) {
        int pow = term1.getPow() + term2.getPow();
        if (pow == 0) {
            return new Term();
        } else {
            return new Term(pow);
        }
    }

    public Expr Mul(Expr left, Expr right) {
        Expr expr = new Expr();
        for (Object o : left.getPoly().entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            Term term1 = (Term) entry.getKey();
            BigInteger big1 = (BigInteger) entry.getValue();
            for (Object a : right.getPoly().entrySet()) {
                Map.Entry enTry = (Map.Entry) a;
                Term term2 = (Term) enTry.getKey();
                BigInteger big2 = (BigInteger) enTry.getValue();
                expr.addTerm(mulTerm(term1, term2), (big1.multiply(big2)));
            }
        }
        return expr;
    }

    public Expr Pow(Expr bot, Integer pow) {
        if (pow == 0) {
            return new Expr(BigInteger.valueOf(1));
        } else if (pow == 1) {
            return bot;
        } else {
            Expr expr;
            expr = Mul(bot, bot);
            for (int i = 2; i < pow; i++) {
                expr = Mul(expr, bot);
            }
            return expr;
        }
    }

}
