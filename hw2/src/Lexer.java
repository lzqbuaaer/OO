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
                lexAdd(tokens);
                pos++;
            } else if (input.charAt(pos) == '-') {
                lexSub(tokens);
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
            } else if (input.charAt(pos) == 'y') {
                tokens.add(new Token(Token.Type.Y, "y"));
                pos++;
            } else if (input.charAt(pos) == 'z') {
                tokens.add(new Token(Token.Type.Z, "z"));
                pos++;
            } else if (input.charAt(pos) == 'f') {
                tokens.add(new Token(Token.Type.F, "f"));
                pos++;
            } else if (input.charAt(pos) == 'g') {
                tokens.add(new Token(Token.Type.G, "g"));
                pos++;
            } else if (input.charAt(pos) == 'h') {
                tokens.add(new Token(Token.Type.H, "h"));
                pos++;
            } else if (input.charAt(pos) == 'e') {
                tokens.add(new Token(Token.Type.EXP, "exp"));
                pos += 3;
            } else if (input.charAt(pos) == ',') {
                tokens.add(new Token(Token.Type.COMMA, ","));
                pos++;
            } else if (input.charAt(pos) >= '0' && input.charAt(pos) <= '9') {
                pos = lexNum(input, tokens, pos);
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

    public void lexAdd(ArrayList<Token> tokens) {
        if (tokens.isEmpty() || tokens.get(tokens.size() - 1).IsAddSub()) {
            tokens.add(new Token(Token.Type.ADD, "+"));
        }
    }

    public void lexSub(ArrayList<Token> tokens) {
        if (tokens.isEmpty() || tokens.get(tokens.size() - 1).IsAddSub()) {
            tokens.add(new Token(Token.Type.SUB, "-"));
        } else {
            tokens.get(tokens.size() - 1).change();
        }
    }

    public int lexNum(String input, ArrayList<Token> tokens, int pos) {
        char now = input.charAt(pos);
        int ans = pos;
        StringBuilder sb = new StringBuilder();
        while (now >= '0' && now <= '9') {
            sb.append(now);
            ans++;
            if (ans >= input.length()) {
                break;
            }
            now = input.charAt(ans);
        }
        tokens.add(new Token(Token.Type.NUM, sb.toString()));
        return ans;
    }
}
