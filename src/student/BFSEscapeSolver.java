package student;

import game.Node;

import java.util.Collection;

/**
 * BFS escape solver
 * Refactored class created by chris on 24/02/2016.
 *
 * @author Chris Grocott
 */
public class BFSEscapeSolver implements EscapeSolver {
    private Node start;
    private Node end;
    private Collection<Node> vertices;
    private int timeLimit;
    private SearchMap map;

    public BFSEscapeSolver(Node start, Node end, Collection<Node> vertices, int timeLimit) {
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
