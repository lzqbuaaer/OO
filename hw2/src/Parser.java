import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class Parser {
    private final Lexer lexer;
    private final HashMap<String, Function> functions;

    public Parser(Lexer lexer, HashMap<String, Function> functions) {
        this.lexer = lexer;
        this.functions = functions;
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
        if (lexer.now().getType() == Token.Type.X ||
                lexer.now().getType() == Token.Type.Y ||
                lexer.now().getType() == Token.Type.Z) {
            return parserPow();
        } else if (lexer.now().getType() == Token.Type.LPAREN) {
            return parserExpFac();
        } else if (lexer.now().getType() == Token.Type.EXP) {
            return parserExp();
        } else if (lexer.now().getType() == Token.Type.F ||
                lexer.now().getType() == Token.Type.G ||
                lexer.now().getType() == Token.Type.H) {
            return parserFuncFac();
        } else {
            return parserNum();
        }
    }

    public Pow parserPow() {
        String variable = lexer.now().getContent();
        lexer.move();
        if (lexer.notEnd() && lexer.now().getType() == Token.Type.POWER) {
            lexer.move();
            if (lexer.now().getType() == Token.Type.ADD) {
                lexer.move();
            }
            Pow pow = new Pow(new BigInteger(lexer.now().getContent()), variable);
            lexer.move();
            return pow;
        } else {
            return new Pow(BigInteger.ONE, variable);
        }
    }

    public ExpFac parserExpFac() {
        lexer.move();
        Expr expr = parserExpr();
        lexer.move();
        if (lexer.notEnd() && lexer.now().getType() == Token.Type.POWER) {
            lexer.move();
            if (lexer.now().getType() == Token.Type.ADD) {
                lexer.move();
            }
            ExpFac expFac = new ExpFac(new BigInteger(lexer.now().getContent()), expr);
            lexer.move();
            return expFac;
        } else {
            return new ExpFac(BigInteger.ONE, expr);
        }
    }

    public Num parserNum() {
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

    public Exp parserExp() {
        lexer.move();
        lexer.move();
        Factor factor = parserFactor();
        lexer.move();
        if (lexer.notEnd() && lexer.now().getType() == Token.Type.POWER) {
            lexer.move();
            if (lexer.now().getType() == Token.Type.ADD) {
                lexer.move();
            }
            Exp exp = new Exp(new BigInteger(lexer.now().getContent()), factor);
            lexer.move();
            return exp;
        } else {
            return new Exp(BigInteger.ONE, factor);
        }
    }

    public FuncFac parserFuncFac() {
        final String name = lexer.now().getContent();
        lexer.move();
        lexer.move();
        ArrayList<Factor> factors = new ArrayList<>();
        factors.add(parserFactor());
        while (lexer.notEnd() && lexer.now().getContent().equals(",")) {
            lexer.move();
            factors.add(parserFactor());
        }
        lexer.move();
        return new FuncFac(functions.get(name), factors);
    }
}
