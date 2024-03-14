public class Token {
    public enum Type {
        ADD, SUB, MUL, LPAREN, RPAREN, NUM, X, Y, Z, POWER, EXP, F, G, H, COMMA
    }

    private Type type;

    private String content;

    public Token(Type type, String content) {
        this.type = type;
        this.content = content;
    }

    public Type getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public boolean IsAddSub() {
        return (!content.equals("+") && !content.equals("-"));
    }

    public void change() {
        if (type == Type.ADD) {
            type = Type.SUB;
            content = "-";
        } else if (type == Type.SUB) {
            type = Type.ADD;
            content = "+";
        }
    }

    public Token copy() {
        return new Token(this.type, this.content);
    }
}