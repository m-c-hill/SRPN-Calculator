import java.util.Stack;

/**
 * IntStack extends the Stack class to create a Stack which can only hold a maximum number of integer values. The SRPN
 * calculator uses this stack to hold integers in memory before performing operations on them and storing the results.
 * It also adds key functionality to the Stack, such as the display method to view the contents.
 */

public class IntStack extends Stack<Integer>{

    // The original SRPN calculator can only hold 23 integers in memory, therefore IntStack has an upper limit on size.
    private final int maxSize;

    public IntStack(int maxSize){
        super();
        this.maxSize = maxSize;
    }

    /**
     * Push method to add items to the stack. If the stack already contain the maximum number of elements, then the
     * "Stack overflow" message is printed.
     * @param item integer value to be pushed to the stack
     */
    public void push(int item){
        if(super.size() < maxSize){
            super.push(item);
        } else{
            System.out.println("Stack overflow.");
        }
    }

    /**
     * Peep method to print the item on top of the stack (most recently added). If the stack size is zero, then the
     * "Stack empty" message is printed.
     */
    public void peep(){
        if (super.size() == 0){
            System.out.println("Stack empty.");
        } else {
            System.out.println(super.peek());
        }
    }

    /**
     * Display method to print the item all items in the stack in reverse order.
     */
    public void display(){
        super.forEach(System.out::println);
    }
}