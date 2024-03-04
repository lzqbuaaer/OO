import java.util.Iterator;

public class Simplify {

    public Simplify() {
    }

    public Expression SimplifyExpression(Expression expression) {
        Expression result = new Expression();
        Iterator<Term> iter = expression.CalculateIndex().GetTerms().iterator();
        while (iter.hasNext()) {
            result.AddTerm(SimplifyTerm(iter.next()));
        }
        return result;
    }

    public Term SimplifyTerm(Term term) {
        Term result = new Term();
        Term outside = new Term();
        outside.AddFactor(term.BuildAllNumber());
        outside.AddFactor(term.BuildAllVariable());
        if (term.HaveExpression()) { //有表达式因子
            Term inside = new Term();
            Expression insider = new Expression();
            Iterator<Factor> iter = term.GetFactors().iterator();
            while (iter.hasNext()) {
                Factor factor = iter.next();
                if (factor instanceof Expression) {
                    insider = this.SimplifyExpression((Expression) factor);
                    break;
                }
            }
            while (iter.hasNext()) {
                Factor factor = iter.next();
                if (factor instanceof Expression) {
                    insider = insider.MultWithExpression(
                            this.SimplifyExpression((Expression) factor));
                }
            }
            Expression finalresult = new Expression();
            Iterator<Term> iter2 = insider.GetTerms().iterator();
            while (iter2.hasNext()) {
                finalresult.AddTerm(this.SimplifyTerm(outside.MultWithTerm(iter2.next())));
            }
            result.AddFactor(finalresult);
            return result;
        } else { //没有表达式因子
            return outside;
        }
    }

}
