import java.util.ArrayList;

public class Lexer {
    private final ArrayList<Token> tokens = new ArrayList<>();
    private int cur = 0;

    public Lexer(String input) {
        int pos = 0;
        while (pos < input.length()) {
            if (input.charAt(pos) == '(') {
                tokens.add(new Token(Token.Type.LPAREN, "("));
                pos++;
            } else if (input.charAt(pos) == ')') {
                tokens.add(new Token(Token.Type.RPAREN, ")"));
                pos++;
            } else if (input.charAt(pos) == '+') {
                if (tokens.isEmpty() || tokens.get(tokens.size() - 1).IsAddSub()) {
                    tokens.add(new Token(Token.Type.ADD, "+"));
                }
                pos++;
            } else if (input.charAt(pos) == '-') {
                if (tokens.isEmpty() || tokens.get(tokens.size() - 1).IsAddSub()) {
                    tokens.add(new Token(Token.Type.SUB, "-"));
                } else {
                    tokens.get(tokens.size() - 1).change();
                }
                pos++;
            } else if (input.charAt(pos) == '*') {
                tokens.add(new Token(Token.Type.MUL, "*"));
                pos++;
            } else if (input.charAt(pos) == '^') {
                tokens.add(new Token(Token.Type.POWER, "^"));
                pos++;
            } else if (input.charAt(pos) == 'x') {
                tokens.add(new Token(Token.Type.X, "x"));
                pos++;
            } else if (input.charAt(pos) >= '0' && input.charAt(pos) <= '9') {
                char now = input.charAt(pos);
                StringBuilder sb = new StringBuilder();
                while (now >= '0' && now <= '9') {
                    sb.append(now);
                    pos++;
                    if (pos >= input.length()) {
                        break;
                    }
                    now = input.charAt(pos);
                }
                tokens.add(new Token(Token.Type.NUM, sb.toString()));
            } else {
                pos++;
            }
        }
    }

    public void move() {
        cur++;
    }

    public Token now() {
        return tokens.get(cur);
    }

    public boolean notEnd() {
        return cur < tokens.size();
    }
}
