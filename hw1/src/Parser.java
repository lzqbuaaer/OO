import java.util.ArrayList;

public class Parser {
    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public Expr parserExpr() {
        ArrayList<Term> terms = new ArrayList<>();
        ArrayList<Token> ops = new ArrayList<>();
        if (lexer.now().getContent().equals("-") || lexer.now().getContent().equals("+")) {
            ops.add(lexer.now());
            lexer.move();
        } else {
            ops.add(new Token(Token.Type.ADD, "+"));
        }
        terms.add(parserTerm());
        while (lexer.notEnd() && (lexer.now().getContent().equals("-") ||
                lexer.now().getContent().equals("+"))) {
            ops.add(lexer.now());
            lexer.move();
            terms.add(parserTerm());
        }
        return new Expr(terms, ops);
    }

    public Term parserTerm() {
        ArrayList<Factor> factors = new ArrayList<>();
        int mark = 1;
        if (lexer.now().getContent().equals("-")) {
            mark = -1;
        }
        factors.add(parserFactor());
        while (lexer.notEnd() && lexer.now().getContent().equals("*")) {
            lexer.move();
            factors.add(parserFactor());
        }
        return new Term(mark, factors);
    }

    public Factor parserFactor() {
        if (lexer.now().getType() == Token.Type.X) {
            lexer.move();
            if (lexer.notEnd() && lexer.now().getType() == Token.Type.POWER) {
                lexer.move();
                if (lexer.now().getType() == Token.Type.ADD) {
                    lexer.move();
                }
                Pow pow = new Pow(Integer.parseInt(lexer.now().getContent()));
                lexer.move();
                return pow;
            } else {
                return new Pow(1);
            }
        } else if (lexer.now().getType() == Token.Type.LPAREN) {
            lexer.move();
            Expr expr = parserExpr();
            lexer.move();
            if (lexer.notEnd() && lexer.now().getType() == Token.Type.POWER) {
                lexer.move();
                if (lexer.now().getType() == Token.Type.ADD) {
                    lexer.move();
                }
                int index = Integer.parseInt(lexer.now().getContent());
                ExpFac expFac = new ExpFac(index, expr);
                lexer.move();
                return expFac;
            } else {
                return new ExpFac(1, expr);
            }
        } else {
            Num num;
            if (lexer.now().getType() == Token.Type.SUB) {
                lexer.move();
                num = new Num("-" + lexer.now().getContent());
            } else if (lexer.now().getType() == Token.Type.ADD) {
                lexer.move();
                num = new Num(lexer.now().getContent());
            } else {
                num = new Num(lexer.now().getContent());
            }
            lexer.move();
            return num;
        }
    }
}
