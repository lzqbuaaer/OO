import java.util.ArrayList;
import java.util.HashMap;

public class Expr {
    private final ArrayList<Term> terms = new ArrayList<>();
    private final ArrayList<Token> ops = new ArrayList<>();

    public Expr(ArrayList<Term> terms, ArrayList<Token> ops) {
        this.terms.addAll(terms);
        this.ops.addAll(ops);
    }

    public Expr substitute(HashMap<String, Factor> inputs) {
        ArrayList<Token> opsCopy = new ArrayList<>();
        for (Token op : this.ops) {
            opsCopy.add(op.copy());
        }
        ArrayList<Term> termsCopy = new ArrayList<>();
        for (Term term : this.terms) {
            termsCopy.add(term.substitute(inputs));
        }
        return new Expr(termsCopy, opsCopy);
    }

    public Polynomial toPoly() {
        Polynomial poly = new Polynomial();
        for (int i = 0; i < terms.size(); i++) {
            if (ops.get(i).getType() == Token.Type.ADD) {
                poly = poly.addPoly(terms.get(i).toPoly());
            } else {
                poly = poly.subPoly(terms.get(i).toPoly());
            }
        }
        return poly;
    }
}
