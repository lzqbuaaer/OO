import java.util.ArrayList;

public class Expr {
    private final ArrayList<Term> terms = new ArrayList<>();
    private final ArrayList<Token> ops = new ArrayList<>();

    public Expr(ArrayList<Term> terms, ArrayList<Token> ops) {
        this.terms.addAll(terms);
        this.ops.addAll(ops);
    }

    public Polynomial count() {
        Polynomial result = new Polynomial();
        for (int i = 0; i < terms.size(); i++) {
            Token op = ops.get(i);
            if (op.getType() == Token.Type.ADD) {
                result.add(terms.get(i).count());
            } else {
                result.sub(terms.get(i).count());
            }
        }
        return result;
    }
}
