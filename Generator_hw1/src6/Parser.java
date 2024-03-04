import java.util.ArrayList;

public class Parser {
    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public Expr parseExpr() {
        ArrayList<Term> terms = new ArrayList<>();
        ArrayList<Token> ops = new ArrayList<>();
        terms.add(parserTerm());
        while (lexer.notEnd() && (lexer.now().getType() == Token.Type.ADD
                || lexer.now().getType() == Token.Type.SUB)) {
            ops.add(lexer.now());
            lexer.next();
            terms.add(parserTerm());
        }
        lexer.next(); //跳过）
        if (lexer.notEnd() && lexer.now().getType().equals(Token.Type.CARET)) {
            lexer.next(); //跳过 ^
            String exp = lexer.now().getContent();
            return new Expr(terms, ops, exp);
        }
        lexer.back();
        return new Expr(terms, ops, "1");
    }

    public Term parserTerm() {
        ArrayList<Factor> factors = new ArrayList<>();
        if (lexer.now().getType().equals(Token.Type.SUB)) {
            factors.add(new Number("-1"));
            lexer.next();
        }
        factors.add(paserFactor());
        while (lexer.notEnd() && lexer.now().getType().equals(Token.Type.MUL)) {
            lexer.next(); //跳过*
            factors.add(paserFactor());
        }
        return new Term(factors);
    }

    public Factor paserFactor() {
        if (lexer.now().getType().equals(Token.Type.NUM)) {
            Number num = new Number(lexer.now().getContent());
            lexer.next();
            return num;
        }
        else if (lexer.now().getType().equals(Token.Type.SUB)) {
            lexer.next();
            Number number = new Number("-" + lexer.now().getContent());
            lexer.next();
            return number;
        }
        else if (lexer.now().getType().equals(Token.Type.VAR)) {
            Token token = lexer.now(); //记录x
            lexer.next();
            if (lexer.notEnd() && lexer.now().getType().equals(Token.Type.CARET)) {
                lexer.next();
                String exp = lexer.now().getContent();
                lexer.next();
                return new Var(token.getContent(), exp);
            }
            return new Var(token.getContent(), "1");
        }
        else {
            lexer.next();
            Expr expr  = parseExpr();
            lexer.next();
            return expr;
        }
    }
}
