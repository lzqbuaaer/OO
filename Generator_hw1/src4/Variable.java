public class Variable extends Factor {
    public Expr unfold() {
        Expr newExpr = new Expr(this);
        return newExpr;
    }
}
