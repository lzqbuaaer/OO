public class Lexer {
    private final String input;
    private int pos = 0;
    private String curToken;

    public Lexer(String input) {
        this.input = input;
        this.next();
    }

    private String getSymbol() {
        int symbol; // 符号
        if (input.charAt(pos) == '-') {
            symbol = -1;
        }
        else {
            symbol = 1;
        }
        ++pos;
        while (pos < input.length() &&
                ((input.charAt(pos) == '+')
                        || (input.charAt(pos) == ' ')
                        || (input.charAt(pos) == '\t')
                        || (input.charAt(pos) == '-'))) {
            if (input.charAt(pos) == '-') {
                symbol = -symbol;
            }
            ++pos;
        }
        if (symbol < 0) {
            return "-";
        }
        else {
            return "+";
        }
    }

    private String getNumber() {
        StringBuilder sb = new StringBuilder();
        int frontZero = 1; // 前零
        while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
            if (input.charAt(pos) != '0') {
                frontZero = 0;
            }
            if (frontZero == 0) {
                sb.append(input.charAt(pos));
            }
            ++pos;
        }
        if (sb.length() == 0) {
            sb.append("0");
        }
        return sb.toString();
    }

    public void next() {
        if (pos >= input.length()) {
            return;//输入完了
        }
        while (input.charAt(pos) == ' ' || input.charAt(pos) == '\t') {
            pos += 1;
        }
        char c = input.charAt(pos);
        if (Character.isDigit(c)) {
            curToken = this.getNumber();
        }
        else if (c == '-' || c == '+') {
            curToken = this.getSymbol();
        }
        else if (c == '*' || c == '^' || c == '(' || c == ')'
                || Character.isAlphabetic(c)) {
            pos += 1;
            curToken = String.valueOf(c);
        }
        else {
            System.out.println("Input Format Error. Forbidden Char Included.");
            pos = input.length();
        }
    }

    public String peek() {
        return this.curToken;
    }
}
