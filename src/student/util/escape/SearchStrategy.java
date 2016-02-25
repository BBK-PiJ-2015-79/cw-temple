package student.util.escape;

import java.io.Serializable;
import java.util.Comparator;

/**
 * A SearchStrategy is a Comparator geared towards Nodes in an escape map. Concrete
 * implementations of SearchStrategy are passed through to searchMaps to provide a
 * mechanism for weighting nodes when constructing the search Map.
 *
 * Created by chris on 23/02/2016.
 *
 * @author Chris Grocott
 */
public interface SearchStrategy extends Comparator<EscapeNodeStatus>, Serializable {
    @Override
    int compare(EscapeNodeStatus n1, EscapeNodeStatus n2);
}
