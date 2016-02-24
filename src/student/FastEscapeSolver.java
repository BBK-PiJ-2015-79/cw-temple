package student;

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
public class FastEscapeSolver implements EscapeSolver {
    private Node start; // do I even need this?
    private Node end;
    private Collection<Node> vertices; // do I even need this?
    private int timeLimit; // do I even need this?
    private SearchMap map;

    public FastEscapeSolver(Node start, Node end, Collection<Node> vertices, int timeLimit) {
        this.start = start;
        this.end = end;
        this.vertices = vertices;
        this.timeLimit = timeLimit;
        this.map = setupSearchMap(start, end, vertices);
    }

    @Override
    public EscapePath getPath() {
        return map.getPathTo(end);
    }

    private SearchMap setupSearchMap(Node start, Node end, Collection<Node> vertices) {
        return new SearchMap(start, end, vertices, new FastSearchStrategy());
    }
}
