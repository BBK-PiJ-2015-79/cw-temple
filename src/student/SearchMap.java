package student;

import game.Node;

import java.util.*;
import java.util.stream.Collectors;

import static student.MappingStatus.MAPPED;
import static student.MappingStatus.PENDING;
import static student.MappingStatus.UNMAPPED;

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

    /**
     * Constructor
     * @param start - the start node of the map
     * @param end - the end node of the map (the one, ultimately that will be searched for
     * @param vertices - the nodes which make up the escape cavern
     * @param strategy - the search strategy used to construct the search map
     */
    public SearchMap(Node start, Node end, Collection<Node> vertices, SearchStrategy strategy) {
        // TODO decide whether multiple constructors are really neccessary
        this.vertices = vertices;
        this.start = start;
        this.end = end;
        this.strategy = strategy;
        this.map = new HashMap<>();
        initialiseMap();
        buildSearchMap();
    }

    private void initialiseMap() {
        // TODO
        for(Node node: vertices) {
            map.put(node, new EscapeNodeStatus(node.getId()));
        }
    }

    private void buildSearchMap() {
        //TODO possible refactoring of EscapeNodeStatus update procedure?
        LinkedList<Node> searchQueue = new LinkedList<>();
        Set<Node> currentUnvisitedNeighbours = new HashSet<>();

        searchQueue.addLast(start);
        map.get(start).setNodeStatus(PENDING);
        map.get(start).setSearchDepth(0);
        map.get(start).setPathDistance(0);
        map.get(start).setGoldOnPath(0);
        //Do BFS with prioritisation given by search strategy
        while(!searchQueue.isEmpty()) {
            Node currentNode = searchQueue.removeFirst();
            // Get the next nodes to add to the queue
            currentUnvisitedNeighbours = currentNode.getNeighbours().stream().filter(n -> {
                //statement style to make more readable
                return map.get(n).getNodeStatus() == UNMAPPED;
            }).collect(Collectors.toSet());

            for(Node node : currentUnvisitedNeighbours) {
                int previousGold;
                previousGold = map.get(currentNode).getGoldOnPath();
                map.get(node).setPredecessor(currentNode);
                map.get(node).setNodeStatus(PENDING);
                map.get(node).setSearchDepth(map.get(currentNode).getSearchDepth() + 1);
                map.get(node).setPathDistance(map.get(currentNode).getPathDistance()
                        + currentNode.getEdge(node).length());
                map.get(node).setGoldOnPath(previousGold + node.getTile().getGold());
                searchQueue.add(node);
            }

            map.get(currentNode).setNodeStatus(MAPPED);
            searchQueue.sort((n1,n2) -> strategy.compare(map.get(n1), map.get(n2)));
        }
    }



    // TODO implementation of getPath - decide whether this needs to be a linked list or a path?
    public EscapePath getPathTo(Node finalNode) {
        // TODO implement this.
        EscapePath returnPath = new EscapePath();
        Node currentNode = finalNode;
        Node nextNode = null;
        returnPath.addFirst(currentNode);
        while(map.get(currentNode).getSearchDepth() > 0) {
            nextNode = map.get(currentNode).getPredecessor();
            returnPath.addFirst(nextNode);
            currentNode = nextNode;
        }
        return returnPath;
    }

    //TODO possible candidate for deletion? don't want to expose more of the map than I need to
//    public Map<Node, EscapeNodeStatus> getMap() {
//        return map;
//    }

    public EscapeNodeStatus getNodeStatus(Node someNode) {
        return map.get(someNode);
    }

}
