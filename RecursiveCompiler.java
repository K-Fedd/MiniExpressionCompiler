import java.util.*;

public class RecursiveCompiler {

    // main method requests equation. printing its token stream, a depth tree, and the result as an integer
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // ask user for math equation using only +, -, *, /, (, ) operators and integers
        System.out.println("Please input a mathematical expression with...\nonly +, -, *, /, (, ) operators and integers:");
        String equation = scanner.nextLine();

        // attempts to parse equation for token stream, depth tree, and sum
        try {
            // create node root to equal the equation evaluated
            Node root = evaluate(equation);

            // print tokens from left to right
            System.out.println("\nTOKENS: " + tokens);

            // use root node to print depth tree
            printByDepth(root);

            // set int result to equal the root node, which is the sum of the equation
            int result = evaluateNode(root);
            System.out.println("\nEvaluation Result: " + result);

        } catch (Exception e) {
            System.out.println("\nError: " + e.getMessage());
        }

        scanner.close();
    }

    // node class to hold a value and point to a left and right node
    static class Node {
        String value;
        Node left, right;
        Node(String v) { value = v; }
        Node(String v, Node l, Node r) { value = v; left = l; right = r; }
    }

    // create string expr to be a duplicate of the original,
    // int pos to keep track of position in the list,
    // and the list of tokens to print them in order
    private static String expr;
    private static int pos;
    private static List<String> tokens;

    // entry point to solve equations
    public static Node evaluate(String s) {

        // remove spaces from original equation string,
        // set int position to zero to start at the beginning of the equation
        expr = s.replaceAll(" ", "");
        pos = 0;
        tokens = new ArrayList<>();

        // check for invalid characters in the string
        validateCharacters(expr);

        // check for invalid operator sequences BEFORE parsing
        validateOperatorSequences(expr);

        // parse through the equation
        Node root = parseExpression();

        // if position is not equal to the fully parsed expression give an error
        if (pos != expr.length())
            error("Unexpected trailing characters");

        // return root node
        return root;
    }

    // validate characters of a string equation
    private static void validateCharacters(String s) {
        // for loop rotates through equation validating each character
        for (int i = 0; i < s.length(); i++) {
            // set character c to the character and position I in the equation
            char c = s.charAt(i);

            // return an error if the character isn't a number or operator
            if (!Character.isDigit(c) && "+-*/()".indexOf(c) == -1) {
                errorAt(i, "Invalid character '" + c + "'");
            }
        }
    }

    // validate operator sequences
    private static void validateOperatorSequences(String s) {

        // checks to ensure there arent multiple operators one after the other
        for (int i = 0; i < s.length() - 1; i++) {
            char c1 = s.charAt(i);
            char c2 = s.charAt(i + 1);

            boolean op1 = "+-*/".indexOf(c1) != -1;
            boolean op2 = "+-*/)".indexOf(c2) != -1;

            // if there are two operators in a row produce an error
            if (op1 && op2) {
                errorAt(i + 1, "Unexpected operator '" + c2 + "' after '" + c1 + "'");
            }
        }

        // expression cannot start with operator except ( or produce error
        if (s.length() > 0 && "+*/".indexOf(s.charAt(0)) != -1)
            errorAt(0, "Expression cannot start with operator '" + s.charAt(0) + "'");

        // expression cannot end with operator or produce error
        char last = s.charAt(s.length() - 1);
        if ("+-*/".indexOf(last) != -1)
            errorAt(s.length() - 1, "Expression cannot end with operator '" + last + "'");
    }

    // error methods run highlight method to reveal showcase error location
    private static void error(String msg) {
        throw new RuntimeException(msg + "\n" + highlight(pos));
    }

    private static void errorAt(int index, String msg) {
        throw new RuntimeException(msg + "\n" + highlight(index));
    }

    // print equation and highlights the index position where the first error was located
    private static String highlight(int index) {
        StringBuilder sb = new StringBuilder();
        // prints equation and jumps to new line
        sb.append(expr).append("\n");

        // place spaces based on error location so ^ character is pointing to the correct area
        for (int i = 0; i < index; i++) sb.append(" ");
        sb.append("^ error at index ").append(index);
        return sb.toString();
    }

    // validates addition and subtraction operators
    private static Node parseExpression() {
        Node left = parseTerm();

        while (pos < expr.length()) {
            char ch = expr.charAt(pos);

            if (ch == '+' || ch == '-') {
                tokens.add("" + ch);
                pos++;

                // if the position of the character is greater than
                // or equal to the expressions length, produce an error
                if (pos >= expr.length())
                    error("Expression cannot end with operator '" + ch + "'");

                Node right = parseTerm();
                left = new Node(String.valueOf(ch), left, right);

            } else break;
        }
        return left;
    }

    // validates multiplication and dividision operators
    private static Node parseTerm() {
        Node left = parseFactor();

        while (pos < expr.length()) {
            char ch = expr.charAt(pos);

            if (ch == '*' || ch == '/') {
                tokens.add("" + ch);
                pos++;

                // if the position of the character is greater than
                // or equal to the expressions length, produce an error
                if (pos >= expr.length())
                    error("Expression cannot end with operator '" + ch + "'");

                Node right = parseFactor();
                left = new Node(String.valueOf(ch), left, right);

            } else break;
        }
        return left;
    }

    // parses equation
    private static Node parseFactor() {
        // if the position of the character is greater than
        // or equal to the expressions length, produce an error
        if (pos >= expr.length())
            error("Unexpected end of expression");

        char ch = expr.charAt(pos);

        // if theres an opening parenthesis, ensure they arent empty or hanging
        if (ch == '(') {
            tokens.add("(");
            pos++;

            // cannot have empty parentheses
            if (pos < expr.length() && expr.charAt(pos) == ')')
                error("Empty parentheses not allowed");

            // parse expression from inside of parenthesis
            Node value = parseExpression();

            // cannot have no closing parenthesis
            if (pos >= expr.length() || expr.charAt(pos) != ')')
                errorAt(pos, "Missing closing parenthesis");

            tokens.add(")");
            
            // removes closing parenthesis
            pos++;
            return value;
        }

        // cannot have a digit where there should be an operator
        if (!Character.isDigit(ch))
            error("Expected number but found '" + ch + "'");

        int start = pos;
        int num = 0;

        // checks the digits after each operator and adds them into one number
        while (pos < expr.length() && Character.isDigit(expr.charAt(pos))) {
            num = num * 10 + (expr.charAt(pos) - '0');
            pos++;
        }

        // add numbers to token string list
        tokens.add(expr.substring(start, pos));
        return new Node(String.valueOf(num));
    }

    // operator node checks below nodes and applies operator to numbers, 
    // replacing the value of the operator node with the number
    private static int evaluateNode(Node n) {
        // if there are no more nodes below node n, return the value in n
        if (n.left == null && n.right == null)
            return Integer.parseInt(n.value);

        int L = evaluateNode(n.left);
        int R = evaluateNode(n.right);

        // switch applies the node to the left and right ints
        return switch (n.value) {
            case "+" -> L + R;
            case "-" -> L - R;
            case "*" -> L * R;
            case "/" -> {
                // produce error if dividing by zero
                if (R == 0) throw new RuntimeException("Division by zero");
                yield L / R;
            }
            // produce error if operator isn't supported
            default -> throw new RuntimeException("Invalid operator: " + n.value);
        };
    }

    // print the trees by its depth
    public static void printByDepth(Node root) {
        // creates a map to tie each depth to list of characters,
        // which will contain the characters at that depth
        Map<Integer, List<String>> levels = new TreeMap<>();
        // collects the depth of each operator and integer
        collectDepth(root, 0, levels, null);

        // prints out the depth tree
        System.out.println("\nTREE BY DEPTH:");
        for (var entry : levels.entrySet()) {
            System.out.print("Depth " + entry.getKey() + ": ");
            System.out.println(String.join(", ", entry.getValue()));
        }
    }

    // collects the depth of each operator in the equation
    private static void collectDepth(Node node, int depth, Map<Integer, List<String>> levels, Node parent) {
        if (node == null) return;

        // sets the value of each nodes parent to be none if the parent is null
        // this is needed for the highest depth operators
        String parentLabel = (parent == null ? "none": parent.value);

        // creates a new array list to represent a depth if it does not yet exist, and adding the parent nodes value
        // if there is one. if the parents value is null then only store the nodes value
        if (parentLabel != "none")
            levels.computeIfAbsent(depth, k -> new ArrayList<>()).add("(" + parentLabel + ") " + node.value);
        else
            levels.computeIfAbsent(depth, k -> new ArrayList<>()).add(node.value);
        // collect the depth of the nodes below the current depth of nodes
        collectDepth(node.left, depth + 1, levels, node);
        collectDepth(node.right, depth + 1, levels, node);
    }

}
