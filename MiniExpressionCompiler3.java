import java.util.*;

public class MiniExpressionCompiler3 {
    public static void main(String[] args) {
        //create scanner to read user input
        java.util.Scanner scanner = new java.util.Scanner(System.in);

        //prompt user for math equation, including valid operators
        System.out.println("Please input a mathematical expression with...\nonly +, -, *, /, (, ) operators and integers:");
        String equation = scanner.nextLine();
        
        // evaluate the expression and print the result
        try {
            int result = evaluateExpression(equation);
            System.out.println("Evaluation Result: " + result);
        } catch (Exception e) {
            System.out.println("Error evaluating expression: " + e.getMessage());
        }

        scanner.close();
    } // charAt(ch)

    private static int For(Stack<Integer> nums, Stack<Character> ops) throws Exception {
        Stack<Character> opsDupe = new Stack<>();
        Stack<Character> opsDupeReverse = new Stack<>();

        Stack<Integer> numsDupe = new Stack<>();
        Stack<Integer> numsDupeReverse = new Stack<>();

        int sum;
        int totalOps = 0;

        Stack<Integer> precedenceOperators = new Stack<>();
        Stack<Integer> reversePrecedenceOperators = new Stack<>();

        Stack<Integer> higherPrecedenceNumber = new Stack<>();
        Stack<Integer> reverseHigherPrecedenceNumber = new Stack<>();

        Stack<Integer> lowerPrecedenceNumber = new Stack<>();
        Stack<Integer> reverseLowerPrecedenceNumber = new Stack<>();

        Stack<Character> higherPrecedenceOperator = new Stack<>();
        Stack<Character> reverseHigherPrecedenceOperator = new Stack<>();

        Stack<Character> lowerPrecedenceOperator = new Stack<>();
        Stack<Character> reverseLowerPrecedenceOperator = new Stack<>();

        // Destroys opsDupe, splitting operators into two stacks based on precedence
        while (ops.size() > 0) {

            // if top operator is a closing parenthesis run For again from the next opening parenthesis
            if (ops.peek() == ')'){
                ops.pop();

                // create new stack to hold operators and numbers within parentheses
                while (ops.peek() != '(') {
                    numsDupe.push(nums.pop());
                    opsDupe.push(ops.pop());
                }
                numsDupe.push(nums.pop());
                // reverse opsDupe to maintain original order
                while (opsDupe.size() > 0) {
                    numsDupeReverse.push(numsDupe.pop());
                    opsDupeReverse.push(opsDupe.pop());
                }
                // pop the opening parenthesis
                ops.pop();

                // push result of For back onto nums
                nums.push(For(numsDupeReverse, opsDupeReverse));
            }

            if ("+-*/".indexOf() != -1) {
                totalOps++;
                if (op == '+' || op == '-') {
                    orderOfOperations.push(1);
                    lowerPrecedenceOperator.push(ops.pop());
                    precedenceOperators.push(1);
                    lowerPrecedenceNumber.push(nums.pop());
                }
                else if (op == '*' || op == '/') {
                    orderOfOperations.push(2);
                    higherPrecedenceOperator.push(ops.pop());
                    precedenceOperators.push(2);
                    higherPrecedenceNumber.push(nums.pop());
                }
                lowerPrecedenceNumber.push(nums.pop());
            }
        }

        // Reverse stacks to maintain original order
        while (lowerPrecedence.size() > 0) {
            reverseLowerPrecedenceNumber.push(lowerPrecedenceNumber.pop());
            reverseLowerPrecedenceOperator.push(lowerPrecedenceOperator.pop());
        }
        while (higherPrecedence.size() > 0) {
            reverseLowerPrecedenceNumber.push(higherPrecedenceNumber.pop());
            reverseHigherPrecedenceOperator.push(higherPrecedenceOperator.pop());
        }
        while (precedenceOperators.size() > 0) {
            reversePrecedenceOperators.push(precedenceOperators.pop());
        }

        // Apply higher precedence operations first
        while (reverseHigherPrecedenceOperator.size() > 0) {
            Stack<Integer> reversePrecedenceOperatorsDupe = new Stack<>();

            char op = reverseHigherPrecedenceOperator.pop();
            int y = reverseHigherPrecedenceNumber.pop();
            int x;

            while (reversePrecedenceOperatorsDupe.peek() != 2)
                reversePrecedenceOperatorsDupe.pop();
            reversePrecedenceOperatorsDupe.pop();
            if (reversePrecedenceOperatorsDupe.peek() != 2)
                x = reverseLowerPrecedenceNumber.pop();
            else
                x = reverseHigherPrecedenceNumber.pop();

            if (op = "*")
                sum = y * x;
            else
                sum = y / x;
        }

        // Apply lower precedence operations
        while (reverseHigherPrecedenceOperator.size() > 0) {
            Stack<Integer> reversePrecedenceOperatorsDupe = new Stack<>();

            char op = reverseHigherPrecedenceOperator.pop();
            int y = reverseHigherPrecedenceNumber.pop();
            int x;

            while (reversePrecedenceOperatorsDupe.peek() != 2)
                reversePrecedenceOperatorsDupe.pop();
            reversePrecedenceOperatorsDupe.pop();
            if (reversePrecedenceOperatorsDupe.peek() != 2)
                x = reverseLowerPrecedenceNumber.pop();
            else
                x = reverseHigherPrecedenceNumber.pop();

            if (op = "*")
                sum = y * x;
            else
                sum y / x;
        }





        
        while (operatorValues.size() > 0) {
            reverseStack.push(operatorValues.pop);

            operatorValues.push(ForLoop(nums, ops, totalOps));
        }
    }

    // Evaluate mathematical expression
    public static int evaluateExpression(String expression) throws Exception {
        // Initialize tokens string
        String tokens = "";
        
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

            // Append character to tokens string
            tokens = tokens + Character.toString(ch) + ", ";

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
                operators.push();
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

        // Remove trailing comma and space
        tokens = tokens.substring(0, tokens.length() - 2);
        System.out.println("Tokens: [" + tokens + "]");

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

