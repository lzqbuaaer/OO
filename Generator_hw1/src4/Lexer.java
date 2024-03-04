public class Lexer {
    private final String input;
    private int pos = 0;
    private String curToken;

    public Lexer(String input) {
        this.input = input;
        this.next();
    }

    private String getNumber() {
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
            sb.append(input.charAt(pos));
            ++pos;
        }

        return sb.toString();
    }

    public String next() {
        if (pos == input.length()) {
            return this.curToken;
        }

        char c = input.charAt(pos);
        if (Character.isDigit(c)) {
            curToken = getNumber();
        } else {
            pos += 1;
            curToken = String.valueOf(c);
        }

        return this.curToken;
    }

    public String peek() {
        return this.curToken;
    }

    public char charAtPos() {
        if (pos < input.length()) {
            return input.charAt(pos);
        }
        return '\0';
    }
}
