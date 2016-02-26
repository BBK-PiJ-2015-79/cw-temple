package student.util.escape;

import game.Node;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A SearchMap is a structure describing possible paths through a given EscapeState. The paths are
 * constructed using a breadth-first exploration strategy with the assumption that you want to get from
 * one node to another specified node. A node weighting is specified at construction by passing in an object
 * which implements EscapeStrategy.
 *
 * Once a SearchMap has been constructed paths can be found by specifying a destination Node (typically the exit
 * node but the class has deliberately been left open to calculate paths to other nodes) and then following the
 * search tree from that destination back up the hierarchy until the root node is reached.
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

    /**
     * Constructor
     * @param start - the start node of the map
     * @param end - the end node of the map (the one, ultimately that will be searched for
     * @param vertices - the nodes which make up the escape cavern
     * @param strategy - the search strategy used to construct the search map
     */
    public SearchMap(Node start, Node end, Collection<Node> vertices, SearchStrategy strategy) {
        this.vertices = vertices;
        this.start = start;
        this.end = end;
        this.strategy = strategy;
        this.map = new HashMap<>();
        buildSearchMap();
    }

    /*
     * Builds the search map, the nodes are processed in breadth-first order and processed according
     * to the strategy defined by the SearchStrategy object passed in at construction.
     */
    //TODO can this be split out in to two separate methods?
    private void buildSearchMap() {
        initialiseMap();
        PriorityQueue<Node> searchQueue = new PriorityQueue<>((n1,n2) -> strategy.compare(map.get(n1), map.get(n2)));

        searchQueue.add(start);
        updateEscapeNodeStatus(start, null);
        //Do BFS with prioritisation given by search strategy
        while(!searchQueue.isEmpty()) {
            Node currentNode = searchQueue.remove();
            // Get unprocessed neighbouring nodes and add them to the queue
            Set<Node> currentUnvisitedNeighbours = currentNode.getNeighbours().stream()
                    .filter(n -> map.get(n).getNodeStatus() == MappingStatus.UNMAPPED).collect(Collectors.toSet());
            for(Node neighbour : currentUnvisitedNeighbours) {
                updateEscapeNodeStatus(neighbour, currentNode);
                searchQueue.add(neighbour);
            }
            map.get(currentNode).setNodeStatus(MappingStatus.MAPPED); // we're done with this node
        }
    }

    /*
     * Helper function for buildSearchMap() - puts all of the nodes in the current escape
     * state in to the search map and initialises their corresponding EscapeNodeStatus.
     */
    private void initialiseMap() {
        for(Node node: vertices) {
            map.put(node, new EscapeNodeStatus(node.getId()));
        }
    }

    /*
     * Helper function to update the EscapeNodeStatus of nodes before
     * being added to the search queue.
     */
    private void updateEscapeNodeStatus(Node currentNode, Node parentNode) {
        EscapeNodeStatus currentNodeStatus = map.get(currentNode);
        EscapeNodeStatus parentNodeStatus = map.get(parentNode);

        currentNodeStatus.setPredecessor(parentNode); //0: predecessor
        currentNodeStatus.setNodeStatus(MappingStatus.PENDING); //1: status
        if(currentNode.equals(start)) {
            currentNodeStatus.setSearchDepth(0); //2: depth
            currentNodeStatus.setPathDistance(0); //3: distance
            currentNodeStatus.setGoldOnPath(0); //4: gold
        }
        else {
            int previousGold = parentNodeStatus.getGoldOnPath();
            currentNodeStatus.setSearchDepth(parentNodeStatus.getSearchDepth() + 1); //2: depth
            currentNodeStatus.setPathDistance(parentNodeStatus.getPathDistance()
                    + parentNode.getEdge(currentNode).length());  //3: distance
            currentNodeStatus.setGoldOnPath(previousGold + currentNode.getTile().getGold()); //4: gold
        }
    }

    /**
     * Returns an EscapePath from the start position of this SearchMap to the node specified as a parameter.
     *
     * This method is useful for finding partial routes through an EscapeCavern
     * @param finalNode the destination node of the path.
     * @return an EscapePath going from the initial node to the destination node.
     */
    public EscapePath getPathTo(Node finalNode) {
        EscapePath returnPath = new EscapePath();
        Node currentNode = finalNode;
        returnPath.addFirst(currentNode);
        while(map.get(currentNode).getSearchDepth() > 0) {
            Node nextNode = map.get(currentNode).getPredecessor();
            returnPath.addFirst(nextNode);
            currentNode = nextNode;
        }
        return returnPath;
    }

    /**
     * Returns an escape path from the start position of this SearchMap to the end position.
     * @return an EscapePath from the start node to the end node.
     */
    public EscapePath getPathToEnd() {
        return getPathTo(end);
    }

    /**
     * Get the EscapeNodeStatus corresponding to a given node.
     * @param someNode the node to be examined.
     * @return the EscapeNodeStatus object corresponding to the desired Node.
     */
    public EscapeNodeStatus getNodeStatus(Node someNode) {
        return map.get(someNode);
    }

}
