import java.util.*;

public class RecursiveCompiler {
    public static void main(String[] args) {
        //create scanner to read user input
        java.util.Scanner scanner = new java.util.Scanner(System.in);

        //prompt user for math equation, including valid operators
        System.out.println("Please input a mathematical expression with...\nonly +, -, *, /, (, ) operators and integers:");
        String equation = scanner.nextLine();

        // evaluate the expression and print the result
        try {
            int result = evaluate(equation);
            printTokens();
            System.out.println("Evaluation Result: " + result);
        } catch (Exception e) {
            System.out.println("Error evaluating expression: " + e.getMessage());
        }

        scanner.close();
    }


    private static String expr;
    private static int pos;

    public static int evaluate(String s) {
        expr = s.replaceAll(" ", "");
        pos = 0;
        return parseExpression();
    }

    // expression = term { (+|-) term }
    private static int parseExpression() {
        int value = parseTerm();
        while (pos < expr.length()) {
            char ch = expr.charAt(pos);
            if (ch == '+' || ch == '-') {
                pos++;
                int right = parseTerm();
                if (ch == '+') value += right;
                else value -= right;
            } else break;
        }
        return value;
    }

    // term = factor { (*|/) factor }
    private static int parseTerm() {
        int value = parseFactor();
        while (pos < expr.length()) {
            char ch = expr.charAt(pos);
            if (ch == '*' || ch == '/') {
                pos++;
                int right = parseFactor();
                if (ch == '*') value *= right;
                else value /= right;
            } else break;
        }
        return value;
    }

    // factor = number | '(' expression ')'
    private static int parseFactor() {
        char ch = expr.charAt(pos);

        // Parenthesis
        if (ch == '(') {
            pos++;
            int value = parseExpression();
            pos++;  // skip ')'
            return value;
        }

        // Number
        int num = 0;
        while (pos < expr.length() && Character.isDigit(expr.charAt(pos))) {
            num = num * 10 + (expr.charAt(pos) - '0');
            pos++;
        }
        return num;
    }

    private static void printTokens() {
        ArrayList<Character> list = new ArrayList<>();
        for (char c: expr.toCharArray()) {
            list.add(c);
        }
        System.out.println("Tokens: " + list);
    }
}