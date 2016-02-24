package student;

import game.Node;

import java.util.LinkedList;

/**
 * An escape path is a collection of nodes. The nodes should
 * all be next to each other, so you should be able to move from
 * one to the next for a given state.
 *
 * Created by chris on 24/02/2016.
 *
 * @author Chris Grocott
 */
public class EscapePath {
    LinkedList<Node> path;

    public EscapePath() {
        //TODO zero argument constructor;
        path = new LinkedList<>();
    }

    //TODO
    public int getGoldOnPath() {
        return 0;
    }

    //TODO
    public Boolean isPathLenghOkay(int upperLimit) {
        // should path even be responsible for this? probably not
        return false;
    }

    //TODO
    public int getPathLength() {
        return 0;
    }

    //TODO
    public EscapePath getReversePath() {
        return null;
    }

    //TODO
    public EscapePath concatenatePaths() {
        return null;
    }

    //TODO - also, is this a good name?
    public int size() {
        return path.size();
    }

    public void addFirst(Node someNode) {
        path.addFirst(someNode);
    }

    public void addLast(Node someNode) {
        path.addLast(someNode);
    }

    public Node get(int index) throws IndexOutOfBoundsException {
        return path.get(index);
    }

}
