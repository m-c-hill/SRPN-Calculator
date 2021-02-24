/**
 * Floating point exception used to emulate the error generated when the original SRPN calculator attempts to apply the
 * modulo operator when the divisor is zero (ie. 6 % 0 will raise this exception and exit the program).
 */

public class FloatingPointException extends RuntimeException {
    public FloatingPointException(String message){
        super(message);
    }
}