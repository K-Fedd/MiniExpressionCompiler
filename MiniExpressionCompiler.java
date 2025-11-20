import java.util.*;

public class MiniExpressionCompiler {
    public static void main(String[] args) {
        //create scanner to read user input
        java.util.Scanner scanner = new java.util.Scanner(System.in);

        //prompt user for math equation, including valid operators
        System.out.println("Please input a mathematical expression with...\nonly +, -, *, /, (, ) operators and integers:");
        String equation = scanner.nextLine();

        // evaluate the expression and print the result
        try {
            int result = evaluateExpression(equation);
            System.out.println("= " + result);
        } catch (Exception e) {
            System.out.println("Error evaluating expression: " + e.getMessage());
        }

        scanner.close();
    }

    // 
    public static int evaluateExpression(String expression) throws Exception {
        // Remove spaces
        expression = expression.replaceAll(" ", "");

        // Initialize parenthesis counters
        int OpeningParenthesisCount = 0;
        int ClosingParenthesisCount = 0;

        // Create stacks for integers and operators
        Stack<Integer> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();

        int i = 0;
        while (i < expression.length()) {
            // Get the current character
            char ch = expression.charAt(i);

            if (i>0) {
                // Check for multiplication implied by parentheses
                parenthesesMultiplication(expression.charAt(i - 1), ch);
            }

            // If the character is a digit, parse the full number
            if (Character.isDigit(ch)) {
                //sets number value to zero
                int num = 0;
                // while int at position i is shorter than the expression length and is a digit set the number to be itself times 10 plus the new digit
                while (i < expression.length() && Character.isDigit(expression.charAt(i))) {
                    num = num * 10 + (expression.charAt(i) - '0');
                    i++;
                }
                numbers.push(num);
                continue; // Skip the increment of i since it's already updated
            }

            // if the character is '('
            if (ch == '(') {
                // increment opening parenthesis count and push to operator stack
                OpeningParenthesisCount++;
                operators.push(ch);
            }

            // if the chacter is ')'
            else if (ch == ')') {
                // Solve expression within parentheses
                while (!operators.isEmpty() && operators.peek() != '(') {
                    applyTopOperator(numbers, operators);
                }
                if (operators.isEmpty()) {
                    throw new Exception("Mismatched parentheses.");
                }
                // increment closing parenthesis count and pop from operator stack
                ClosingParenthesisCount++;
                operators.pop();
            }

            // if the character is any other operator
            else if ("+-*/".indexOf(ch) != -1) {
                // while operator stack isnt empty, the top operator isnt '(', and the peeked operator has precedence over the current character
                while (!operators.isEmpty() && operators.peek() != '(' && hasPrecedence(operators.peek(), ch)) {
                    applyTopOperator(numbers, operators);
                }
                operators.push(ch);
            }

            // if the character is any other character
            else {
                throw new Exception("Invalid character in expression: " + ch);
            }

            // increment i
            i++;
        }

        if (OpeningParenthesisCount != ClosingParenthesisCount) {
            throw new Exception("Mismatched parentheses.");
        }

        // Apply remaining operations
        while (!operators.isEmpty()) {
            char op = operators.pop();
            // if popped operator is '(' throw exception
            if (op == '(') {
                throw new Exception("Mismatched parentheses. More opening parentheses than closing parentheses.");
            }
            // apply operator
            applyOperator(numbers, op);
        }

        return numbers.pop();
    }

    // Apply operator to the top two numbers in the stack
    private static void applyTopOperator(Stack<Integer> numbers, Stack<Character> operators) throws Exception {
        char op = operators.pop();
        applyOperator(numbers, op);
    }

    // Apply specific operator to the top two numbers in the stack
    private static void applyOperator(Stack<Integer> numbers, char op) throws Exception {
        int y = numbers.pop();
        int x = numbers.pop();

        // perform operation based on operator
        switch (op) {
            case '+':
                numbers.push(x + y);
                break;
            case '-':
                numbers.push(x - y);
                break;
            case '*':
                numbers.push(x * y);
                break;
            case '/':
                // check for division by zero
                if (y == 0) {
                    throw new Exception("Cannot divide by zero.");
                }
                numbers.push(x / y);
                break;
            // if operator is invalid throw exception
            default:
                throw new Exception("Invalid operator: " + op);
        }
    }

    // Check operator precedence
    private static boolean hasPrecedence(char op1, char op2) {
        // return true if operator 1 has a higher or equal precedence than operator 2
        return precedence(op1) >= precedence(op2);
    }

    // returns a precedence value for each operator
    private static int precedence(char op) {
        if (op == '+' || op == '-') return 1;
        if (op == '*' || op == '/') return 2;
        return 0;
    }

    // check for multiplication implied by parentheses
    private static void parenthesesMultiplication(char op1, char op2) throws Exception {
        // check for multiplication implied by left parentheses
        boolean multiplyFromLeftParemthesis = (Character.isDigit(op1) || op1 == ')') && op2 == '(';
        // check for multiplication implied by right parentheses
        boolean multiplyFromRightParenthesis = op1 == ')' && Character.isDigit(op2);

        // throw exception if either case is true
        if (multiplyFromLeftParemthesis || multiplyFromRightParenthesis) {
            throw new Exception("Multiplication implied by parentheses is not supported. Please use '*' operator explicitly.");
        }
    }
}
