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
            // Build the parse tree (AST)
            Node root = evaluate(equation);

            // Print tree by depth
            printByDepth(root);

            int result = evaluateNode(root);
            System.out.println("\nEvaluation Result: " + result);

        } catch (Exception e) {
            System.out.println("Error evaluating expression: " + e.getMessage());
        }

        scanner.close();
    }

    // === ADDED NODE CLASS ===
    static class Node {
        String value;
        Node left, right;

        Node(String v) { value = v; }
        Node(String v, Node l, Node r) { value = v; left = l; right = r; }
    }

    private static String expr;
    private static int pos;

    // CHANGED: now returns Node instead of int
    public static Node evaluate(String s) {
        expr = s.replaceAll(" ", "");
        pos = 0;
        return parseExpression();
    }

    // expression = term { (+|-) term }
    private static Node parseExpression() {
        Node left = parseTerm();

        while (pos < expr.length()) {
            char ch = expr.charAt(pos);

            if (ch == '+' || ch == '-') {
                pos++;
                Node right = parseTerm();
                left = new Node(String.valueOf(ch), left, right);
            } else break;
        }
        return left;
    }

    // term = factor { (*|/) factor }
    private static Node parseTerm() {
        Node left = parseFactor();

        while (pos < expr.length()) {
            char ch = expr.charAt(pos);

            if (ch == '*' || ch == '/') {
                pos++;
                Node right = parseFactor();
                left = new Node(String.valueOf(ch), left, right);
            } else break;
        }
        return left;
    }

    // factor = number | '(' expression ')'
    private static Node parseFactor() {
        char ch = expr.charAt(pos);

        // Parenthesis
        if (ch == '(') {
            pos++;

            Node value = parseExpression();

            pos++;  // skip ')'

            return value;
        }

        // Number
        int num = 0;
        while (pos < expr.length() && Character.isDigit(expr.charAt(pos))) {
            num = num * 10 + (expr.charAt(pos) - '0');
            pos++;
        }

        return new Node(String.valueOf(num));
    }

    private static int evaluateNode(Node n) {
        if (n.left == null && n.right == null) {
            return Integer.parseInt(n.value);
        }

        int L = evaluateNode(n.left);
        int R = evaluateNode(n.right);

        return switch (n.value) {
            case "+" -> L + R;
            case "-" -> L - R;
            case "*" -> L * R;
            case "/" -> L / R;
            default -> throw new RuntimeException("Invalid operator: " + n.value);
        };
    }

// Print each depth level of the parse tree
public static void printByDepth(Node root) {
    Map<Integer, List<String>> levels = new TreeMap<>();
    collectDepth(root, 0, levels);

    System.out.println("\nTREE BY DEPTH:");
    for (var entry : levels.entrySet()) {
        System.out.print("Depth " + entry.getKey() + ": ");
        System.out.println(String.join(", ", entry.getValue()));
    }
}

// Recursive helper: collect node values grouped by depth
private static void collectDepth(Node node, int depth, Map<Integer, List<String>> levels) {
    if (node == null) return;

    levels.computeIfAbsent(depth, k -> new ArrayList<>()).add(node.value);

    collectDepth(node.left, depth + 1, levels);
    collectDepth(node.right, depth + 1, levels);
}

}
