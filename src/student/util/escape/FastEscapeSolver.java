package student.util.escape;

import game.Node;

import java.util.Collection;

/**
 * Fast escape solver
 *
 * This uses a BFS strategy and prioritises the shortest paths for exploration
 * at each search depth. It won't get much gold but should provide a safety net
 * if all else fails.
 *
 * Refactored class created by chris on 24/02/2016.
 *
 * @author Chris Grocott
 */
// TODO: 25/02/2016 decide whether timeLimit checking is needed - what to do in case of issues?
public class FastEscapeSolver implements EscapeSolver {
    private SearchMap map;

    public FastEscapeSolver(Node start, Node end, Collection<Node> vertices, int timeLimit) {
        this.map = setupSearchMap(start, end, vertices);
    }

    @Override
    public EscapePath getPath() {
        return map.getPathToEnd();
    }

    private SearchMap setupSearchMap(Node start, Node end, Collection<Node> vertices) {
        return new SearchMap(start, end, vertices, new FastSearchStrategy());
    }
}
