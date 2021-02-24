import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Creates an instance of the SRPN calculator and continues to take input from the user. This input is processed by
 * calling the processCommand() method.
*/

public class Main {

    public static void main(String[] args) {

        SRPN calculator = new SRPN();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            while(true) {
                String command = reader.readLine();
                if(command == null){
                    System.exit(0);
                }
                else{
                    if (!command.isEmpty()) {
                        // Process the user's input
                        calculator.processCommand(command);
                    }
                }
            }
        }
        catch(IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
