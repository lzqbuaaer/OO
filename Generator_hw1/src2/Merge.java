import java.util.Iterator;

public class Merge {
    public Merge() {

    }

    public Expression MergeExpression(Expression expression) {
        Expression result = new Expression();
        Iterator<Term> iter = expression.GetTerms().iterator();
        while (iter.hasNext()) {
            Term a = iter.next(); //a为项
            if (a.HaveExpression()) { //a只有唯一一个表达式
                Iterator<Factor> iter2 = a.GetFactors().iterator();
                while (iter2.hasNext()) {
                    result.AddWithExpression(this.MergeExpression((Expression) iter2.next()));
                }
            }
            else { //是项
                result.AddTerm(a);
            }
        }
        return result;
    }
}
