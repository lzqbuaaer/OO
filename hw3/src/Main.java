import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = Integer.parseInt(scanner.nextLine().trim());
        HashMap<String, Function> func = new HashMap<>();
        for (int i = 0; i < n; i++) {
            String str = scanner.nextLine().trim();
            String key = String.valueOf(str.charAt(0));
            Function value = new Function(str, func);
            func.put(key, value);
        }
        Lexer lexer = new Lexer(scanner.nextLine());
        Parser parser = new Parser(lexer, func);
        Expr expr = parser.parserExpr();
        Polynomial poly = expr.toPoly();
        System.out.println(poly.toString());
    }
}
