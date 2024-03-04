import java.math.BigInteger;

public class Parser {
    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public Expr parseExpr() {
        Expr expr = new Expr();
        expr.addTerm(parseTerm());
        while (lexer.peek().equals("+") || lexer.peek().equals("-")) {
            if (lexer.peek().equals("-")) {
                lexer.next();
                expr.addTerm(parseTerm().negate());
            }
            else {
                lexer.next();
                expr.addTerm(parseTerm());
            }
        }
        return expr;
    }

    public Term parseTerm() {
        Term term = new Term();
        term.addFactor(parseFactor());
        while (lexer.peek().equals("*")) {
            lexer.next();
            term.addFactor(parseFactor());
        }
        return term;
    }

    public Factor parseBrackets() {
        lexer.next();
        Factor expr = parseExpr();
        lexer.next();
        if (lexer.peek().equals("^")) {
            lexer.next();
            if (lexer.peek().equals("+")) {
                lexer.next();
            }
            int exponent = Integer.parseInt(lexer.peek());
            expr = expr.powered(exponent);
            lexer.next();
        }
        return expr;
    }

    public Factor parseVariables() {
        Variable v = new Variable(lexer.peek());
        lexer.next();
        if (lexer.peek().equals("^")) {
            lexer.next();
            if (lexer.peek().equals("+")) {
                lexer.next();
            }
            int exponent = Integer.parseInt(lexer.peek());
            if (exponent == 0) {
                lexer.next();
                return new Number(new BigInteger("1"));
            }
            v = v.powered(exponent);
            lexer.next();
        }
        return v;
    }

    public Factor parseFactor() {
        if (lexer.peek().equals("-")) {
            lexer.next();
            if (lexer.peek().equals("(")) {
                return parseBrackets().negate();
            }
            else {
                if (lexer.peek().matches("[A-Za-z]+")) {
                    return parseVariables().negate();
                }
                else {
                    BigInteger num = new BigInteger(lexer.peek());
                    lexer.next();
                    return new Number(num).negate();
                }
            }
        }
        else if (lexer.peek().equals("+")) {
            lexer.next();
        }
        if (lexer.peek().equals("(")) {
            return parseBrackets();
        }
        else {
            if (lexer.peek().matches("[A-Za-z]+")) {
                return parseVariables();
            }
            else {
                BigInteger num = new BigInteger(lexer.peek());
                lexer.next();
                return new Number(num);
            }
        }
    }
}
