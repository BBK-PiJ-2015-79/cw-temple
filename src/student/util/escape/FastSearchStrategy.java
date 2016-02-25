package student.util.escape;

/**
 * A search strategy used to find the fastest way out of an escape map
 *
 * Created by chris on 23/02/2016.
 *
 * @author Chris Grocott
 */
public class FastSearchStrategy implements SearchStrategy {
    @Override
    public int compare(EscapeNodeStatus n1, EscapeNodeStatus n2) {
        return n1.getPathDistance() - n2.getPathDistance();
    }
}
