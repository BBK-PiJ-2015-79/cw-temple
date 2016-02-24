package student;

import game.Node;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO documentation for a search map...
 *
 * Created by chris on 23/02/2016.
 *
 * @author Chris Grocott
 */
public class SearchMap {
    private SearchStrategy strategy;
    private Collection<Node> vertices;
    private Map<Node, EscapeNodeStatus> map;
    private Node start;
    private Node end;

    public SearchMap(Collection<Node> vertices, Node start, Node end) {
        // TODO decide whether multiple constructors are really neccessary
        strategy = null;
        map = new HashMap<>();
        this.vertices = vertices;
        this.start = start;
        this.end = end;
    }

    public SearchMap(Collection<Node> vertices, Node start, Node end, SearchStrategy strategy) {
        // TODO decide whether multiple constructors are really neccessary
        this(vertices, start, end);
        setStrategy(strategy);
    }

    public void setStrategy(SearchStrategy strategy) {
        this.strategy = strategy;
    }

    public void initialiseMap() {
        // TODO
    }

    // TODO implementation of getPath - decide whether this needs to be a linked list or a path?
    public EscapePath getPathTo(Node finalNode) {
        // TODO implement this.
        return null;
    }

}
