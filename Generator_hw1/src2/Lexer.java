public class Lexer {
    private final String input;
    private int pos = 0;
    private String curToken = "";

    private String last;

    public Lexer(String input) {
        this.input = input;
        this.next();
    }

    private String GetNumber() {
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
            sb.append(input.charAt(pos));
            ++pos;
        }

        return sb.toString();
    }

    private String GetVariable() {
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && Character.isLetter(input.charAt(pos))) {
            sb.append(input.charAt(pos));
            ++pos;
        }

        return sb.toString();
    }

    public void next() {
        if (pos == input.length()) {
            return;
        }

        char c = input.charAt(pos);
        if (Character.isDigit(c)) {
            last = curToken;
            curToken = GetNumber();
        } else if (Character.isLetter(c)) {
            last = curToken;
            curToken = GetVariable();
        } else if (c == '(' || c == ')' || c == '*' || c == '+' || c == '-' || c == '^') {
            pos += 1;
            last = curToken;
            curToken = String.valueOf(c);
        }
    }

    public String peek() {
        return this.curToken;
    }

    public String last() {
        return this.last;
    }
}
