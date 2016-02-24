package student;

/**
 * A search strategy used to find a remunerative way out of an escape map
 *
 * Created by chris on 23/02/2016.
 *
 * @author Chris Grocott
 */
public class GreedySearchStrategy implements SearchStrategy {
    @Override
    public int compare(EscapeNodeStatus n1, EscapeNodeStatus n2) {
        return n2.getGoldOnPath() - n1.getGoldOnPath();
    }
}
