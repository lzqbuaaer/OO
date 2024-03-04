
public class Lexer {

    public Lexer() {

    }

    public String preprocess(String input) {
        StringBuilder sb = new StringBuilder();
        if (input.charAt(0) == '+' || input.charAt(0) == '-') {
            sb.append('0');
        }
        for (int i = 0; i < input.length(); i++) {
            int a = 1;
            if (input.charAt(i) == '+' || input.charAt(i) == '-') {
                int j;
                for (j = i; j < input.length() && (input.charAt(j) == '+'
                        || input.charAt(j) == '-'); j++) {
                    if (input.charAt(j) == '-') {
                        a = a * (-1);
                    }
                }
                i = j - 1;
                if (a == 1) {
                    sb.append("+");
                } else {
                    sb.append("-");
                }
            } else {
                sb.append(input.charAt(i));
            }
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < sb.toString().length(); i++) {
            if (sb.toString().charAt(i) == '^' || sb.toString().charAt(i) == '*') {
                if (sb.toString().charAt(i + 1) == '+') {
                    builder.append(sb.toString().charAt(i));
                    i = i + 1;
                } else if (sb.toString().charAt(i + 1) == '-') {
                    builder.append(sb.toString().charAt(i));
                    i = i + 1;
                    builder.append("(-1)*");
                } else {
                    builder.append(sb.toString().charAt(i));
                }
            } else {
                builder.append(sb.toString().charAt(i));
            }
        }
        StringBuilder last = new StringBuilder();
        int pos = 0;
        while (pos < builder.toString().length()) {
            if (builder.toString().charAt(pos) == '0') {
                if (pos < builder.toString().length() - 1 && pos > 0 &&
                        !Character.isDigit(builder.toString().charAt(pos - 1)) &&
                        Character.isDigit(builder.toString().charAt(pos + 1))) {
                    while (pos < builder.toString().length() - 1 && builder.toString().charAt(pos)
                            == '0' && Character.isDigit(builder.toString().charAt(pos + 1))) {
                        ++pos;
                    }
                }
            }
            last.append(builder.toString().charAt(pos));
            ++pos;
        }
        return last.toString();
    }
}
