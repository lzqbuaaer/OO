import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public Expr Parse(String inPut) {
        String input = lexer.preprocess(inPut);
        Pattern numberPattern = Pattern.compile("[a-zA-Z]+");
        int pos = findAdd(input);
        if (pos != -1) {
            if (input.charAt(pos) == '+') {
                return new Tool().Add(Parse(input.substring(0, pos)),
                        Parse(input.substring(pos + 1)));
            } else {
                return new Tool().Sub(Parse(input.substring(0, pos)),
                        Parse(input.substring(pos + 1)));
            }
        } else {
            pos = findMul(input);
            if (pos != -1) {
                return new Tool().Mul(Parse(input.substring(0, pos)),
                        Parse(input.substring(pos + 1)));
            } else {
                if (!input.isEmpty()) {
                    pos = findPow(input);
                    if (pos != -1) {
                        if (input.charAt(pos - 1) == 'x') {
                            return new Expr(Integer.parseInt(
                                    String.valueOf(input.charAt(pos + 1))));
                        } else {
                            return new Tool().Pow(Parse(input.substring(findLparen(input) + 1,
                                            findRparen(input))),
                                    Integer.parseInt(
                                    String.valueOf(input.charAt(pos + 1))));
                        }
                    } else {
                        if (findLparen(input) != -1 && findRparen(input) != -1) {
                            return Parse(input.substring(findLparen(input) + 1, findRparen(input)));
                        } else {
                            Matcher matcher = numberPattern.matcher(input);
                            if (matcher.find()) {
                                return new Expr(1);
                            } else {
                                BigInteger big = new BigInteger(input);
                                return new Expr(big);
                            }
                        }
                    }
                } else {
                    BigInteger zero = new BigInteger("0");
                    return new Expr(zero);
                }
            }
        }

    }

    public int findAdd(String input) {
        int pos = -1;
        int num;
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '(') {
                num = 1;
                while (num != 0) {
                    i++;
                    if (input.charAt(i) == '(') {
                        num++;
                    }
                    if (input.charAt(i) == ')') {
                        num--;
                    }

                }
            }
            if (input.charAt(i) == '+' || input.charAt(i) == '-') {
                pos = i;
            }
        }
        return pos;
    }

    public int findMul(String input) {
        int pos = -1;
        int num;
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '(') {
                num = 1;
                while (num != 0) {
                    i++;
                    if (input.charAt(i) == '(') {
                        num++;
                    }
                    if (input.charAt(i) == ')') {
                        num--;
                    }
                }
            }
            if (input.charAt(i) == '*') {
                pos = i;
            }
        }
        return pos;
    }

    public int findPow(String input) {
        int pos = -1;
        int num;
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '(') {
                num = 1;
                while (num != 0) {
                    i++;
                    if (input.charAt(i) == '(') {
                        num++;
                    }
                    if (input.charAt(i) == ')') {
                        num--;
                    }
                }
            }
            if (input.charAt(i) == '^') {
                pos = i;
            }
        }
        return pos;
    }

    public int findLparen(String input) {
        int begin = -1;
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '(') {
                begin = i;
                break;
            }
        }
        return begin;
    }

    public int findRparen(String input) {
        int end = -1;
        int num = 0;
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '(') {
                num++;
            }
            if (input.charAt(i) == ')') {
                num--;
                if (num == 0) {
                    end = i;
                    break;
                }
            }
        }
        return end;
    }

}
