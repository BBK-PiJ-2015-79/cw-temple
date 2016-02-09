package student;

import game.EscapeState;
import game.Node;

import java.util.LinkedList;

/**
 * Interface for escape solvers, added to make swapping in BFS/DFS algorithms easier
 */
public interface EscapeSolver {
    /**
     * Return an escape path
     * @param start the start point for the path
     * @param end the end point for the path
     * @param state the escape state
     * @return a Linked List with an escape route
     */
    LinkedList<Node> getPath(Node start, Node end, EscapeState state);
}
