package student.util.escape;

import game.Node;

import java.util.*;

/**
 * An escape path is a collection of nodes describing a path through an EscapeCavern. The nodes should
 * all be next to each other, so that the explorer is able to move from one to the next for a given state.
 *
 * Created by chris on 24/02/2016.
 *
 * @author Chris Grocott
 */
public class EscapePath {
    private final List<Node> path;

    public EscapePath() {
        path = new ArrayList<>();
    }

    /**
     * Returns the amount of gold on the path, nodes that are traversed multiple times are only
     * counted once
     * @return the amount of gold on the path
     */
    public int getGoldOnPath() {
        Set<Node> nodeSet = new HashSet<>(path); //eliminate duplicates - no need to stream!
        return nodeSet.stream().mapToInt(n -> n.getTile().getGold()).sum(); // make sure this is safe for empty?
    }

    /**
     * Get the length of the path taking in to account edge weightings
     * @return the weighted length of the path
     */
    public int getPathLength() {
        int length = 0;
        for(int i = 0; i < (path.size() - 1); i++) {
            length += path.get(i).getEdge(path.get(i+1)).length();
        }
        return length;
    }

    /**
     * Returns an EscapePath which is the reverse of the path on which the
     * method is called.
     * @return a reversed EscapePath
     */
    public EscapePath getReversePath() {
        EscapePath rPath = new EscapePath();
        for(int i = 0; i < this.size(); i++) {
            rPath.addFirst(this.get(i));
        }
        return rPath;
    }

    /**
     * Returns an EscapePath which consists of all of the nodes on the path on which the
     * method was called followed by the nodes on the path passed as a parameter (except
     * the first).
     *
     * Note that in order for this method to make sense, the nodes of the second path must
     * 'follow on' from the nodes of the path. That is, the last node in the first path must
     * be the same as the first node in the second path. If this is not the case an
     * IllegalArgumentException is thrown
     * @param somePath the path to be concatenated to the target path
     * @return the concatenated EscapePath
     * @throws IllegalArgumentException
     */
    public EscapePath concatenatePath(EscapePath somePath) throws IllegalArgumentException {
        if(!(this.get(this.size() - 1).equals(somePath.get(0)))) {
            throw new IllegalArgumentException("Paths were not contiguous");
        }
        EscapePath concatenatedPath = new EscapePath();
        for(int i = 0; i < this.size(); i++) {
            concatenatedPath.addLast(this.get(i));
        }
        for(int j = 1; j < somePath.size(); j++) {
            concatenatedPath.addLast(somePath.get(j));
        }
        return concatenatedPath;
    }

    /**
     * The size of the EscapePath in terms of the number of nodes it contains.
     *
     * To find the amount of time it takes to traverse this EscapePath use getPathLength() instead
     * @return the number of nodes in this EscapePath
     */
    public int size() {
        return path.size();
    }

    /**
     * Add a node to the start of the EscapePath
     * @param someNode the node to add
     */
    public void addFirst(Node someNode) {
        path.add(0, someNode);
    }

    /**
     * Add a node to the end of the EscapePath
     * @param someNode the node to add
     */
    public void addLast(Node someNode) {
        path.add((path.size()), someNode);
    }

    /**
     * Get the node at a particular index in the path. The path remains
     * unchanged
     * @param index the index of the node to be returned
     * @return the node at the index specified
     * @throws IndexOutOfBoundsException
     */
    public Node get(int index) throws IndexOutOfBoundsException {
        return path.get(index);
    }

}
