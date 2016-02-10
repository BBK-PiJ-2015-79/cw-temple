package student;

import game.EscapeState;
import game.Node;

import java.util.*;
import java.util.stream.Collectors;
import static student.MappingStatus.*;

/**
 * Created by chris on 09/02/2016.
 */
public class BFSEscapeSolver implements EscapeSolver {
    Map<Node, EscapeNodeStatus> searchMap;
    LinkedList<Node> queue;

    @Override
    public LinkedList<Node> getPath(Node start, Node end, EscapeState state) {

        LinkedList<Node> path;
        setupSearchMap(state);


        queue = new LinkedList<>();
        queue.addLast(start);
        searchMap.get(start).setNodeStatus(PENDING);
        searchMap.get(start).setSearchDepth(0);
        searchMap.get(start).setPathDistance(0);
        //Do BFS
        while(!queue.isEmpty()) {
            //System.out.println(queue);
            Node currentNode = queue.removeFirst();
            // Get the next nodes to add to the queue
            Set<Node> currentUnvisitedNeighbours = currentNode.getNeighbours().stream().filter(n -> {
                //statement style to make more readable
                return searchMap.get(n).getNodeStatus() == UNMAPPED;
            }).collect(Collectors.toSet());
            for(Node node : currentUnvisitedNeighbours) {
                searchMap.get(node).setPredecessor(currentNode);
                searchMap.get(node).setNodeStatus(PENDING);
                searchMap.get(node).setSearchDepth(searchMap.get(currentNode).getSearchDepth() + 1);
                searchMap.get(node).setPathDistance(searchMap.get(currentNode).getPathDistance()
                        + currentNode.getEdge(node).length());
                queue.add(node);
            }
            searchMap.get(currentNode).setNodeStatus(MAPPED);
            queue.sort((n1,n2) -> searchMap.get(n1).getPathDistance() - searchMap.get(n2).getPathDistance());
        }
        path = constructPathFromSearchMap(end);
        return path;
    }

    private void setupSearchMap(EscapeState state) {
        Collection<Node> nodes = state.getVertices();
        searchMap = new HashMap<>();
        for(Node node : nodes) {
            searchMap.put(node, new EscapeNodeStatus(node.getId()));
        }
    }

    private LinkedList<Node> constructPathFromSearchMap(Node end) {
        LinkedList<Node> path = new LinkedList<>();
        Node currentNode = end;
        path.addFirst(currentNode);
        while(searchMap.get(currentNode).getSearchDepth() > 0) {
            Node nextNode = searchMap.get(currentNode).getPredecessor();
            path.addFirst(nextNode);
            currentNode = nextNode;
        }
        return path;
    }
}
