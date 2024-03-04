import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public Expression parseExpr() {
        Expression expr = new Expression();
        expr.AddTerm(parseTerm());

        while (lexer.peek().equals("+") || lexer.peek().equals("-")) {
            lexer.next();
            expr.AddTerm(parseTerm());
        }
        return expr;
    }

    public Term parseTerm() {
        Term term = new Term();
        if (lexer.last().equals("-")) {
            BigInteger num = new BigInteger("-1");
            term.AddFactor(new Number(num));
        }
        if (lexer.peek().equals("+")) {
            lexer.next();
        } else if (lexer.peek().equals("-")) {
            BigInteger num = new BigInteger("-1");
            term.AddFactor(new Number(num));
            lexer.next();
        }
        if (lexer.peek().equals("+")) {
            lexer.next();
        } else if (lexer.peek().equals("-")) {
            BigInteger num = new BigInteger("-1");
            term.AddFactor(new Number(num));
            lexer.next();
        }
        if (lexer.peek().equals("+")) {
            lexer.next();
        } else if (lexer.peek().equals("-")) {
            BigInteger num = new BigInteger("-1");
            term.AddFactor(new Number(num));
            lexer.next();
        }
        term.AddFactor(parseFactor());

        while (lexer.peek().equals("*")) {
            lexer.next();
            if (lexer.peek().equals("+")) {
                lexer.next();
            } else if (lexer.peek().equals("-")) {
                BigInteger num = new BigInteger("-1");
                term.AddFactor(new Number(num));
                lexer.next();
            }
            if (lexer.peek().equals("+")) {
                lexer.next();
            } else if (lexer.peek().equals("-")) {
                BigInteger num = new BigInteger("-1");
                term.AddFactor(new Number(num));
                lexer.next();
            }
            if (lexer.peek().equals("+")) {
                lexer.next();
            } else if (lexer.peek().equals("-")) {
                BigInteger num = new BigInteger("-1");
                term.AddFactor(new Number(num));
                lexer.next();
            }
            term.AddFactor(parseFactor());
        }
        return term;
    }

    public Factor parseFactor() {
        if (lexer.peek().equals("(")) {
            lexer.next();
            Factor expression = parseExpr();
            lexer.next();
            if (lexer.peek().equals("^")) {
                lexer.next();
                if (lexer.peek().equals("+")) {
                    lexer.next();
                    ((Expression)expression).SetIndex(Integer.parseInt(lexer.peek()));
                } else if (lexer.peek().equals("-")) {
                    lexer.next();
                    ((Expression)expression).SetIndex(-Integer.parseInt(lexer.peek()));
                } else {
                    ((Expression)expression).SetIndex(Integer.parseInt(lexer.peek()));
                }
                lexer.next();
            }
            return expression;
        } else {
            Pattern number = Pattern.compile("[0-9]+");
            Matcher matcher = number.matcher(lexer.peek());
            if (matcher.find()) {
                BigInteger num = new BigInteger(lexer.peek());
                lexer.next();
                return new Number(num);
            }
            else {
                Variable variable = new Variable(lexer.peek());
                lexer.next();
                if (lexer.peek().equals("^")) {
                    lexer.next();
                    if (lexer.peek().equals("+")) {
                        lexer.next();
                        variable.SetIndex(Integer.parseInt(lexer.peek()));
                    }
                    else if (lexer.peek().equals("-")) {
                        lexer.next();
                        variable.SetIndex(-Integer.parseInt(lexer.peek()));
                    } else {
                        variable.SetIndex(Integer.parseInt(lexer.peek()));
                    }
                    lexer.next();
                }
                return variable;
            }
        }
    }
}
