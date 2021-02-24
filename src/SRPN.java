/**
 * An instance of the SRPN calculator will receive inputs from the user and split each input into individual tokens.
 * These tokens are then processed: if they are numbers, then they are stored in the IntStack, and if they are
 * operations then the appropriate operation method is called.
 */

public class SRPN {

    IntStack stack;
    private boolean commentOpen = false;
    // Initialise an instance of GNURandom with seed of 1 and size limit of 22, identical to the original SRPN
    private final GNURandom rand = new GNURandom(1, 22);

    /**
     * Constructs an SRPN instance with a stack size of 23 (the original SRPN calculator could only store 23 numbers in
     * memory).
     */
    public SRPN(){
        stack = new IntStack(23);
        System.out.println("You can now start interacting with the SRPN calculator");
    }

    /**
     * Processes an command input by the user by first splitting about any blank spaces into individual commands and
     * then splits into individual tokens of operators and numbers.
     * @param command
     */
    public void processCommand(String command){

        String[] commands = command.split("\\s+");

        for(String c : commands){
            // Regex lookbehind used to ensure any negative symbols associated with a number are kept together (ie.
            // 2*-6 will spilt to 2, *, -6 instead of 2, *, -, 6.
            String[] tokens = c.split("(?<=[^0-9-])|(?=[^0-9])");

            // Equals operators take precedence in the SRPN calculator (ie. *= will execute the '=' symbol first. Since
            // tokens are processed from left to right, the equalsPrecedence method sorts the tokens list such that
            // any '=' symbols adjacent to other operators are moved forward (ie. *,+,= becomes =,*,+).
            tokens = equalsPrecedence(tokens);

            for(String token : tokens){
                // If the token is '#', then commentOpen is inverted
                commentModeCheck(token);

                // As long as comment mode is currently off, each token in the list tokens is processed.
                if ((!commentOpen) && (!token.equals("#"))){
                    processToken(token);
                }
            }
        }
    }

    /**
     * Processes individual tokens parsed in from processCommand. If the token is a number, it is pushed to the stack,
     * while operators are executed.
     * @param token token to be processed
     */
    private void processToken(String token){

        if (isMath(token)) {
            executeMath(token.charAt(0));
        }
        else if(isFunc(token)) {
            executeFunc(token.charAt(0));
        }
        else{
            try{
                double num = Double.parseDouble(token);
                // Check if the number is within the range of integer values (-2147483648 <= <= 2147483647)
                num = Operations.checkSaturation(num);
                this.stack.push((int) num);
            }
            catch (NumberFormatException e) {
                System.out.printf("Unrecognised operator or operand \"%s\"\n", token);
            }
        }
    }

    /**
     * Execute math operations
     * @param operation mathematical operation to be executed
     */
    private void executeMath(char operation) throws FloatingPointException {

        // If the stack size is less than two, then there aren't enough number to process so a stack underflow error
        // message is generated.
        if(stack.size() < 2){
            System.out.println("Stack underflow.");
        }

        else{
            // Retrieve the two most recent numbers from the stack.
            int a = stack.pop();
            int b = stack.pop();

            // If an exception is raised for any of the below operations and the result is unable to be calculated, then
            // the popped values a and b are replaced in the stack using the method replacePoppedValues.

            switch(operation){

                case '+':
                    stack.push(Operations.add(a, b));
                    break;

                case '-':
                    stack.push(Operations.sub(a, b));
                    break;

                case '*':
                    stack.push(Operations.mult(a, b));
                    break;

                case '/':
                    try {
                        stack.push(Operations.intDiv(a, b));
                    } catch (ArithmeticException e){
                        System.out.println("Divide by 0.");
                        replacePoppedValues(a, b);
                    } break;

                case '%':
                    try {
                        stack.push(Operations.modulo(a, b));
                    } catch (ArithmeticException e){
                        System.out.println("Divide by 0.");
                        replacePoppedValues(a, b);
                    }
                    // The floating point error (when divisor = 0) is not caught here since the original program exited
                    // when this error was thrown. Therefore, we also want our program to exit if a floating point error
                    // occurs.
                    break;

                case '^':
                    try {
                        stack.push(Operations.pow(b, a));
                    } catch (ArithmeticException e) {
                        System.out.println("Negative power.");
                        replacePoppedValues(a, b);
                    }
                    break;
            }
        }
    }

    /**
     * Execute the non-mathematical operations of the calculator
     * @param function mathematical operation to be executed
     */
    private void executeFunc(char function){
        switch(function){
            case 'd':
                stack.display();
                break;
            case 'r':
                // Call the next random number from GNURandom and push to the stack.
                stack.push(rand.Next());
                break;
            case '=':
                stack.peep();
                break;
        }
    }

    /**
     * Method to assess if a given token is a mathematical operator or not.
     * @param token token to analyse
     * @return boolean based on if the token is a mathematical operator
     */
    private static boolean isMath(String token){
        String mathOperators = "+-*/^%";
        return mathOperators.contains(token);
    }

    /**
     * Method to assess if a given token is a calculator function or not.
     * @param token token to analyse
     * @return boolean based on if the token is a calculator function
     */
    private static boolean isFunc(String token){
        String funcOperators = "=dr";
        return funcOperators.contains(token);
    }

    /**
     * Method to push two integers to the stack. This is called when an exception is raised by an operator and a result
     * is not available (ie. divide by zero error). The original values must be returned to the stack.
     * @param a value to be pushed to the stack
     * @param b value to be pushed to the stack
     */
    private void replacePoppedValues(int a, int b){
        stack.push(b);
        stack.push(a);
    }

    /**
     * Switches the comment mode on and off. If the incoming token is a "#" character, then the commentOpen value is
     * inverted.
     * @param token token to be analyse
     */
    private void commentModeCheck(String token){
        if(token.equals("#")){
            commentOpen = !commentOpen;
        }
    }

    /**
     * Equals operator takes precedence over other operators it is adjacent to (ie. if we feed in *+= to the calculator,
     * then the = operator will be executed first). Tokens are processed from left to right. This method sorts through a
     * list of tokens in such a way that any = symbol that is adjacent to other operator will be shifted left.
     *
     * Example: [-6, 12, 3, *, -, =, 2, %, =] --> [-6, 12, 3, =, *, -, 2, =, %]
     *
     * @param tokenList list of tokens to be sorted
     * @return sorted list of tokens
     */
    private static String[] equalsPrecedence(String[] tokenList){
        for(int i = 0; i < tokenList.length-1; i++){
            if(isMath(tokenList[i]) && tokenList[i+1].equals("=")){
                tokenList[i+1] = tokenList[i];
                tokenList[i] = "=";
            }
        }
        return tokenList;
    }
}
