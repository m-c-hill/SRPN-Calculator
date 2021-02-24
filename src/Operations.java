/**
 * Mathematical operations for the calculator. When called, the operations are executed on the top two elements in the
 * stack, a and b. A saturation check in each method ensures that the result returned cannot exceed the maximum integer
 * value (2147483647) or be less than the minimum integer value (-2147483648).
 */

public class Operations {

    /**
     * Adds two numbers.
     * @param a number to be added
     * @param b number to be added
     * @return sum of a and b
     */
    public static int add(int a, int b){
        double result = (double) a + (double) b;
        return checkSaturation(result);
    }

    /**
     * Subtracts two numbers.
     * @param a subtrahend
     * @param b minuend
     * @return subtraction of a from b
     */
    public static int sub(int a, int b){
        double result = (double) b - (double) a;
        return checkSaturation(result);
    }

    /**
     * Multiplies two numbers.
     * @param a multiplier
     * @param b multiplicand
     * @return product of a and b
     */
    public static int mult(int a, int b){
        double result = (double) a * (double) b;
        return checkSaturation(result);
    }

    /**
     * Divides two numbers using integer division. Throws an exception if the divisor is equal to zero.
     * @param a divisor
     * @param b dividend
     * @return quotient of b and a
     */
    public static int intDiv(int a, int b){
        if (a == 0){
            throw new ArithmeticException();
        }
        double result = (double) b / (double) a;
        return checkSaturation(result);
    }

    /**
     * Calculates the remainder of a division of two numbers. Throws an exception if the divisor or the dividend is
     * equal to zero.
     * @param a divisor
     * @param b dividend
     * @return remainder of the division of b and a
     */
    public static int modulo(int a, int b){
        if (b == 0){
            throw new ArithmeticException();
        }
        if (a == 0){
            throw new FloatingPointException("Floating point exception (core dumped)");
        }
        double result = (double) b % (double) a;
        return checkSaturation(result);
    }

    /**
     * Calculates the result of a base raised to an exponent. Throws an exception if the exponent is less than 0.
     * @param a base
     * @param b exponent
     * @return a raised to the power of b
     */
    public static int pow(int a, int b){
        if (b < 0){
            throw new ArithmeticException();
        }
        double result = Math.pow(a, b);
        return checkSaturation(result);
    }

    /**
     * Checks if a number is within the standard integer range (-2147483648 <= int <= 2147483647). If below, it returns
     * the minimum integer (-2147483648) and if above, it returns the maximum integer (2147483647). This method is
     * applied to the results of all of the above operator methods.
     * @param result double value to be assessed
     * @return appropriately capped integer
     */
    public static int checkSaturation(double result){
        if (result > Integer.MAX_VALUE){
            return Integer.MAX_VALUE;
        }
        else if(result < Integer.MIN_VALUE){
            return Integer.MIN_VALUE;
        }
        return (int) result;
    }
}