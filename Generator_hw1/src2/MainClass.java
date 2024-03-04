import java.util.Scanner;

public class MainClass {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        Lexer lexer = new Lexer(input.replaceAll(" ", "").replaceAll("\t",""));
        Parser parser = new Parser(lexer);
        Expression initial = parser.parseExpr();
        //
        /*Iterator<Term> iter1 = initial.GetTerms().iterator();
        System.out.println(initial.GetTerms().size() + "个项");
        while (iter1.hasNext()) {
            Term a = iter1.next();
            Iterator<Factor> iter2 = a.GetFactors().iterator();
            System.out.println(a.GetFactors().size() + "个因子，分别为");
            while (iter2.hasNext()) {
                Factor b = iter2.next();
                if (b instanceof Number) {
                    System.out.println("Number " + b);
                } else if (b instanceof Variable) {
                    System.out.println("Variable " + b);
                } else {
                    System.out.println("Expression:" +
                    "(" + b + ")" + "^" + ((Expression)b).GetIndex());
                }
            }
        }*/
        //
        Simplify simplify = new Simplify();
        Expression simplified = simplify.SimplifyExpression(initial);
        //System.out.println("simplified----:" + simplified); //ceshi
        //
        /*Iterator<Term> iter3 = simplified.GetTerms().iterator();
        System.out.println(simplified.GetTerms().size() + "个项");
        while (iter3.hasNext()) {
            Term a = iter3.next();
            Iterator<Factor> iter4 = a.GetFactors().iterator();
            System.out.println(a.GetFactors().size() + "个因子，分别为");
            while (iter4.hasNext()) {
                Factor b = iter4.next();
                if (b instanceof Number) {
                    System.out.println("Number:" + b);
                } else if (b instanceof Variable) {
                    System.out.println("Variable:" + b);
                } else {
                    System.out.println("Expression:" +
                            "(" + b + ")" + "^" + ((Expression)b).GetIndex());
                }
            }
        }*/
        //
        Merge merge = new Merge();
        Expression merged = merge.MergeExpression(simplified);
        //System.out.println(merged); //ceshi
        Short shor = new Short();
        Expression shorted = shor.ShortExpression(merged);
        if (shorted.toString().equals("")) {
            System.out.println("0");
        }
        else if (shorted.toString().charAt(0) == '+') {
            System.out.println(shorted.toString().substring(1));
        } else {
            System.out.println(shorted);
        }
    }
}
