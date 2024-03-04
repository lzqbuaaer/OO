import java.util.Scanner;

public class Main {
    public static void main(String[] in) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        String inPut = input.replaceAll("\\s*", "");
        Lexer lexer = new Lexer();
        System.out.println(new Parser(lexer).Parse(inPut).toString());
    }
}
