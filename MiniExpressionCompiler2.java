import java.util.*;

public class MiniExpressionCompiler2 {

    // Node class for parse tree
    static class Node {
        String value;
        Node left;
        Node right;
        Node(String v) { value = v; }
        Node(String v, Node l, Node r) {
            value = v; left = l; right = r;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please input a mathematical expression with...\nonly +, -, *, /, (, ) operators and integers:");
        String equation = scanner.nextLine();

        try {
            List<String> tokens = tokenize(equation);
            System.out.println("Token Stream: " + tokens);

            Node tree = buildParseTree(tokens);
            System.out.println("Parse Tree:");
            printParseTree(tree, 0);

            int result = evaluateExpression(equation);
            System.out.println("= " + result);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        scanner.close();
    }

    // ---------------- TOKENIZER ----------------
    public static List<String> tokenize(String expr) throws Exception {
        expr = expr.replaceAll(" ", "");
        List<String> tokens = new ArrayList<>();
        int i = 0;
        while (i < expr.length()) {
            char ch = expr.charAt(i);
            if (Character.isDigit(ch)) {
                int j = i;
                while (j < expr.length() && Character.isDigit(expr.charAt(j))) j++;
                tokens.add(expr.substring(i, j));
                i = j;
            } else if ("()+-*/".indexOf(ch) != -1) {
                tokens.add(Character.toString(ch));
                i++;
            } else {
                throw new Exception("Invalid character: " + ch);
            }
        }
        return tokens;
    }

    // ---------------- PARSE TREE CONSTRUCTION ----------------
    public static Node buildParseTree(List<String> tokens) throws Exception {
        Stack<Node> values = new Stack<>();
        Stack<String> ops = new Stack<>();

        for (String tok : tokens) {
            if (tok.matches("\\d+")) {
                values.push(new Node(tok));
            } else if (tok.equals("(")) {
                ops.push(tok);
            } else if (tok.equals(")")) {
                while (!ops.isEmpty() && !ops.peek().equals("(")) {
                    popOperator(values, ops);
                }
                ops.pop();
            } else if ("+-*/".contains(tok)) {
                while (!ops.isEmpty() && !ops.peek().equals("(") &&
                        precedence(ops.peek().charAt(0)) >= precedence(tok.charAt(0))) {
                    popOperator(values, ops);
                }
                ops.push(tok);
            }
        }

        while (!ops.isEmpty()) popOperator(values, ops);
        return values.pop();
    }

    private static void popOperator(Stack<Node> values, Stack<String> ops) {
        String op = ops.pop();
        Node right = values.pop();
        Node left = values.pop();
        values.push(new Node(op, left, right));
    }

    public static void printParseTree(Node node, int depth) {
        if (node == null) return;

        printParseTree(node.right, depth + 1);
        System.out.println("  ".repeat(depth) + node.value);
        printParseTree(node.left, depth + 1);
    }

    // ---------------- EXISTING EVALUATION LOGIC ----------------

    public static int evaluateExpression(String expression) throws Exception {
        expression = expression.replaceAll(" ", "");
        int OpeningParenthesisCount = 0;
        int ClosingParenthesisCount = 0;

        Stack<Integer> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();

        int i = 0;
        while (i < expression.length()) {
            char ch = expression.charAt(i);

            if (i > 0) parenthesesMultiplication(expression.charAt(i - 1), ch);

            if (Character.isDigit(ch)) {
                int num = 0;
                while (i < expression.length() && Character.isDigit(expression.charAt(i))) {
                    num = num * 10 + (expression.charAt(i) - '0');
                    i++;
                }
                numbers.push(num);
                continue;
            }

            if (ch == '(') {
                OpeningParenthesisCount++;
                operators.push(ch);
            } else if (ch == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    applyTopOperator(numbers, operators);
                }
                if (operators.isEmpty()) throw new Exception("Mismatched parentheses.");
                ClosingParenthesisCount++;
                operators.pop();
            } else if ("+-*/".indexOf(ch) != -1) {
                while (!operators.isEmpty() && operators.peek() != '(' && hasPrecedence(operators.peek(), ch)) {
                    applyTopOperator(numbers, operators);
                }
                operators.push(ch);
            } else {
                throw new Exception("Invalid character in expression: " + ch);
            }
            i++;
        }

        if (OpeningParenthesisCount != ClosingParenthesisCount) throw new Exception("Mismatched parentheses.");

        while (!operators.isEmpty()) {
            char op = operators.pop();
            if (op == '(') throw new Exception("Mismatched parentheses.");
            applyOperator(numbers, op);
        }

        return numbers.pop();
    }

    private static void applyTopOperator(Stack<Integer> numbers, Stack<Character> operators) throws Exception {
        char op = operators.pop();
        applyOperator(numbers, op);
    }

    private static void applyOperator(Stack<Integer> numbers, char op) throws Exception {
        int y = numbers.pop();
        int x = numbers.pop();
        switch (op) {
            case '+': numbers.push(x + y); break;
            case '-': numbers.push(x - y); break;
            case '*': numbers.push(x * y); break;
            case '/': if (y == 0) throw new Exception("Cannot divide by zero."); numbers.push(x / y); break;
            default: throw new Exception("Invalid operator: " + op);
        }
    }

    private static boolean hasPrecedence(char op1, char op2) {
        return precedence(op1) >= precedence(op2);
    }

    private static int precedence(char op) {
        if (op == '+' || op == '-') return 1;
        if (op == '*' || op == '/') return 2;
        return 0;
    }

    private static void parenthesesMultiplication(char op1, char op2) throws Exception {
        boolean left = (Character.isDigit(op1) || op1 == ')') && op2 == '(';
        boolean right = op1 == ')' && Character.isDigit(op2);
        if (left || right) throw new Exception("Implicit multiplication not supported. Use '*' explicitly.");
    }
}