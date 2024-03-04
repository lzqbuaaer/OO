import java.util.Scanner;

public class MainClass {    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        Lexer lexer = new Lexer(input.replaceAll("[ \t]", ""));
        Parser parser = new Parser(lexer);

        Expr expr = parser.parseExpr();
        System.out.println(expr.unfold());
        scanner.close();
    }
}