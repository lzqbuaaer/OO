import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Lexer lexer = new Lexer(scanner.nextLine());
        Parser parser = new Parser(lexer);
        Expr expr = parser.parserExpr();
        Polynomial result = expr.count();
        result.print();
    }
}
