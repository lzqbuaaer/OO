import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Function {
    private ArrayList<String> parameters;
    private Expr expr;

    public Function(String func, HashMap<String, Function> funcSet) {
        String[] parts = func.split("=");
        Lexer lexer = new Lexer(parts[1].trim());
        Parser parser = new Parser(lexer, funcSet);
        this.expr = parser.parserExpr();
        this.parameters = initParameters(parts[0]);
    }

    public ArrayList<String> initParameters(String name) {
        Pattern pattern = Pattern.compile("\\((.*?)\\)");
        Matcher matcher = pattern.matcher(name);
        ArrayList<String> parameters = new ArrayList<>();
        if (matcher.find()) {
            String str = matcher.group(1);
            String[] parts = str.split(",");
            for (String para : parts) {
                parameters.add(para.trim());
            }
        }
        return parameters;
    }

    public ArrayList<String> getParameters() {
        return parameters;
    }

    public Expr getExpr() {
        return expr;
    }
}
