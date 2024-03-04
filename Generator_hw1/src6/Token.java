public class Token {
    public enum Type {
        ADD, SUB, MUL, NUM, LPAREN, RPAREN, CARET, VAR
    }

    private final Type type;
    private String content;

    public Token(Type type, String content) {
        this.content = content;
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return content;
    }
}
