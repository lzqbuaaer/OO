import java.math.BigInteger;

public class Parser {
    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public Expr parseExpr() {
        Expr expr = new Expr();

        String curStr = lexer.peek();
        Term newTerm;
        if (curStr.equals("+")) {
            lexer.next();
            newTerm = parseTerm();
            newTerm.setMinus(1);
        } else if (curStr.equals("-")) {
            lexer.next();
            newTerm = parseTerm();
            newTerm.setMinus(-1);
        } else {
            newTerm = parseTerm();
            newTerm.setMinus(1);
        }
        expr.addTerm(newTerm);

        curStr = lexer.peek();
        while (curStr.equals("+") || curStr.equals("-")) {
            lexer.next();
            newTerm = parseTerm();
            if (curStr.equals("+")) {
                newTerm.setMinus(1);
            } else if (curStr.equals("-")) {
                newTerm.setMinus(-1);
            }
            expr.addTerm(newTerm);
            curStr = lexer.peek();
        }
        return expr;
    }

    public Term parseTerm() {
        Term term = new Term();

        String curStr = lexer.peek();
        Factor newFactor;
        if (curStr.equals("+")) {
            lexer.next();
            newFactor = parseFactor();
            newFactor.setMinus(1);
        } else if (curStr.equals("-")) {
            lexer.next();
            newFactor = parseFactor();
            newFactor.setMinus(-1);
        } else {
            newFactor = parseFactor();
            newFactor.setMinus(1);
        }
        term.addFactor(newFactor);

        while (lexer.peek().equals("*")) {
            lexer.next();
            term.addFactor(parseFactor());
        }
        return term;
    }

    public Factor parseFactor() {
        if (lexer.peek().equals("(")) {
            lexer.next();   // curToken: (1x
            Expr expr = parseExpr();
            lexer.next();   // curToken: )^x
            expr.setPower(parseInt());
            return expr;
        } else if (lexer.peek().equals("x")) {
            Variable var = new Variable();
            lexer.next();   // curToken: ^x
            var.setPower(parseInt());
            return var;
        } else {
            BigInteger num;
            if (lexer.peek().equals("+")) {
                lexer.next();
                num = new BigInteger(lexer.peek());
            } else if (lexer.peek().equals("-")) {
                lexer.next();
                num = new BigInteger("-" + lexer.peek());
            } else {
                num = new BigInteger(lexer.peek());
            }
            lexer.next();
            return new Number(num);
        }
    }

    public int parseInt() {
        int pow = 1;
        if (lexer.peek().equals("^")) {
            lexer.next();   // curToken: ^3x
            if (lexer.peek().equals("+")) {
                lexer.next();
                pow = Integer.parseInt(lexer.peek());
            } else if (lexer.peek().equals("-")) {
                lexer.next();
                pow = -1 * Integer.parseInt(lexer.peek());
            } else {
                pow = Integer.parseInt(lexer.peek());
            }
            lexer.next();
        }

        return pow;
    }
}
