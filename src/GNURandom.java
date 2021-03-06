/**
 * The random numbers generated by the original SRPN calculator this project aims to emulate are a result of the GNU C
 * Library's random() function, with the seed set equal to 1. This function provides psuedo-random numbers using a
 * linear additive feedback method.
 *
 * Similar the original SRPN calculator, after a set limit, the random numbers generated by the below algorithm will
 * repeat themselves, looping through the same order as before.
 *
 * The source for this algorithm can be found here: https://www.mathstat.dal.ca/~selinger/random/. The following code
 * is derived from the C program documented by Peter Selinger, Dalhousie University (2007) at the end of this page.
 */

public class GNURandom{

    // Integer array r to store random numbers generated by the below algorithm
    private int[] r;
    private final int seed;
    private final int limit;

    public final int max = 1000;
    private int index = 343;

    /**
     * Constructs the GNURandom random number generator with a specific seed and limit.
     *
     * @param seed  the seed to initiate the pseudorandom algorithm
     * @param limit maximum number of random number generated before repetition
     */
    public GNURandom(int seed, int limit) {
        this.seed = seed;
        this.limit = limit;
        GNURandomNumbers();
    }

    /**
     * Linear additive feedback algorithm to produce pseudo-random numbers in an array, r.
     */
    private void GNURandomNumbers(){
        r = new int[max];
        r[0] = seed;

        for (int i=1; i<31; i++){
            int x = (int) ((16807L * r[i-1]) % Integer.MAX_VALUE);
            if (x < 0){
                x += Integer.MAX_VALUE;
            }
            r[i] = x;
        }

        for (int i = 31; i<34; i++){
            r[i] = r[i-31];
        }

        for (int i = 34; i < 344; i++){
            r[i] = r[i-3] + r[i-31];
        }

        for (int i = 344; i<max; i++){
            r[i] = r[i-31] + r[i-3];
        }
    }

    /**
     * Returns the next pseudo-random number and increments the index by 1. Loops back to the initial random number
     * when the maximum limit of random numbers (defined above) is reached.
     *
     * @return random integer number
     */
    public int Next(){
        index++;
        if(index >= 344 + limit){
            index = 344;
        }
        // Logical right bit shift (unsigned) applied to the random number as per the original algorithm
        return r[index] >>> 1;
    }
}