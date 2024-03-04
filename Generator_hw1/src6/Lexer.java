import java.util.ArrayList;

public class Lexer {
    private int curPos;

    private int pos;
    private final ArrayList<Token> tokens = new ArrayList<>();

    public Lexer(String input) {
        Token topToken = null;
        while (pos < input.length()) {
            if (input.charAt(pos) == '(') {
                topToken = new Token(Token.Type.LPAREN, "(");
                tokens.add(topToken);
                pos++;
            }
            else if (input.charAt(pos) == ')') {
                topToken = new Token(Token.Type.RPAREN, ")");
                tokens.add(topToken);
                pos++;
            }
            else if (input.charAt(pos) == '+') {
                if (topToken != null && (topToken.getType().equals(Token.Type.NUM)
                        || topToken.getType().equals(Token.Type.RPAREN)
                        || topToken.getType().equals(Token.Type.VAR))) {
                    topToken = new Token(Token.Type.ADD, "+");
                    tokens.add(topToken);
                }
                pos++;
            }
            else if (input.charAt(pos) == '*') {
                topToken = new Token(Token.Type.MUL, "*");
                tokens.add(topToken);
                pos++;
            }
            else if (input.charAt(pos) == '-') {
                pos++;
                if (topToken != null && topToken.getType().equals(Token.Type.SUB)) {
                    tokens.remove(topToken);
                    topToken = new Token(Token.Type.ADD, "+");
                }
                else {
                    if (topToken != null && topToken.getType().equals(Token.Type.ADD)) {
                        tokens.remove(topToken);
                    }
                    topToken = new Token(Token.Type.SUB, "-");
                }
                tokens.add(topToken);
            }
            else if (input.charAt(pos) == '^') {
                topToken = new Token(Token.Type.CARET, "^");
                tokens.add(topToken);
                pos++;
            }
            else if (input.charAt(pos) == ' ' || input.charAt(pos) == '\t') {
                pos++;
            }
            else if (input.charAt(pos) == 'x') {
                topToken = new Token(Token.Type.VAR, "x");
                tokens.add(topToken);
                pos++;
            }
            else {
                topToken = new Token(Token.Type.NUM, this.getNumber(input));
                tokens.add(topToken);
            }
        }
    }

    public String getNumber(String input) {
        char num = input.charAt(pos);
        StringBuilder sb = new StringBuilder();
        while (num >= '0' && num <= '9') {
            sb.append(num);
            pos++;
            if (pos < input.length()) {
                num = input.charAt(pos);
            }
            else {
                break;
            }
        }
        return sb.toString();
    }

    public void next() {
        curPos++;
    }

    public void back() {
        curPos--;
    }

    public Token now() {
        return tokens.get(curPos);
    }

    public boolean notEnd() {
        return curPos < tokens.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Token token : tokens) {
            sb.append(token.toString());
        }
        return sb.toString();
    }

}
