package student.util.escape;

import game.Node;

import java.util.Collection;

/**
 * Created by chris on 01/03/2016.
 */
public class GreedyEscapeSolver implements EscapeSolver {
    private SearchMap map;

    public GreedyEscapeSolver(Node start, Node end, Collection<Node> vertices, int timeLimit) {
        this.map = setupSearchMap(start, end, vertices);
    }

    @Override
    public EscapePath getPath() {
        return map.getPathToEnd();
    }

    private SearchMap setupSearchMap(Node start, Node end, Collection<Node> vertices) {
        return new SearchMap(start, end, vertices, new GreedySearchStrategy());
    }
}
