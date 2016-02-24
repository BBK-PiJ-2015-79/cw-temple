package student;

/**
 * A balanced search strategy, halfway between avarice and panic
 *
 * Created by chris on 23/02/2016.
 *
 * @author Chris Grocott
 */
public class ZenSearchStrategy implements SearchStrategy {
    @Override
    public int compare(EscapeNodeStatus n1, EscapeNodeStatus n2) {
        int n1Distance = n1.getPathDistance();
        int n1Gold = n1.getGoldOnPath();
        int n2Distance = n2.getPathDistance();
        int n2Gold = n2.getGoldOnPath();
        int n1Weight = (n1Gold / n1Distance);
        int n2Weight = (n2Gold / n2Distance);
        return n2Weight - n1Weight;
    }
}
