import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Lexer lexer = new Lexer(scanner.nextLine());
        //System.out.println(lexer.toString());
        Parser parser = new Parser(lexer);
        String result = parser.parseExpr().buildPoly().toString();
        //String result = parser.parseExpr().toString();
        System.out.println(result);
    }
}
