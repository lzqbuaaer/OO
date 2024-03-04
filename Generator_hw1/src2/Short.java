import java.util.HashMap;
import java.util.Iterator;

public class Short {
    public Short() {

    }

    public Expression ShortExpression(Expression expression) {
        Expression result = new Expression();
        HashMap<Integer,Term> resultterms = new HashMap<>();
        Iterator<Term> iter = expression.GetTerms().iterator();
        while (iter.hasNext()) {
            Term a = iter.next();
            int i = a.BuildAllVariable().GetIndex();
            if (resultterms.containsKey(i)) {
                resultterms.get(i).AddWithTerm(a);
            } else {
                resultterms.put(i,a);
            }
        }
        for (Integer key : resultterms.keySet()) {
            result.AddTerm(resultterms.get(key));
        }
        return result;
    }
}
