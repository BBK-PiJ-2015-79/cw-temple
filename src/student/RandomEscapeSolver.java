package student;

import game.EscapeState;
import game.Node;

import java.util.*;
import java.util.stream.Collectors;
import static student.MappingStatus.*;

/**
 * Created by chris on 13/02/2016.
 */
public class RandomEscapeSolver implements EscapeSolver {
    Map<Node, EscapeNodeStatus> searchMap;
    LinkedList<Node> queue;
    Random rand;

    @Override
    public LinkedList<Node> getPath(Node start, Node end, EscapeState state) {

        LinkedList<Node> path;
        setupSearchMap(state);
        rand = new Random();

        queue = new LinkedList<>();
        queue.addLast(start);
        searchMap.get(start).setNodeStatus(PENDING);
        searchMap.get(start).setSearchDepth(0);
        searchMap.get(start).setPathDistance(0);
        searchMap.get(start).setGoldOnPath(0);
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
                int previousGold;
                previousGold = searchMap.get(currentNode).getGoldOnPath();
                //if(searchMap.get(node).getPredecessor() == null) {
                //    previousGold = 0;
                //}
                //else {
                //    previousGold = searchMap.get(searchMap.get(node).getPredecessor()).getGoldOnPath();
                //}
                searchMap.get(node).setPredecessor(currentNode);
                searchMap.get(node).setNodeStatus(PENDING);
                searchMap.get(node).setSearchDepth(searchMap.get(currentNode).getSearchDepth() + 1);
                searchMap.get(node).setPathDistance(searchMap.get(currentNode).getPathDistance()
                        + currentNode.getEdge(node).length());
                searchMap.get(node).setGoldOnPath(previousGold + node.getTile().getGold());
                queue.add(node);
            }
            searchMap.get(currentNode).setNodeStatus(MAPPED);
            queue.sort((n1,n2) -> {
                int n1Distance = searchMap.get(n1).getPathDistance();
                int n1Gold = searchMap.get(n1).getGoldOnPath();
                int n2Distance = searchMap.get(n2).getPathDistance();
                int n2Gold = searchMap.get(n2).getGoldOnPath();
                int n1Weight = (n1Gold / n1Distance);
                int n2Weight = (n2Gold / n2Distance);
                return n2Weight - n1Weight;
            });
        }
        //System.out.println("HighestGold: " + getHighestGold());
        path = constructPathFromSearchMap(end);
        return path;
    }

    private int getHighestGold() {
        Set<Node> nodes = searchMap.keySet();
        int higestGold = 0;
        for(Node n : nodes) {
            System.out.println(n.getTile().getGold() + ": " + searchMap.get(n).getGoldOnPath());
            if(searchMap.get(n).getGoldOnPath() > higestGold) {
                higestGold = searchMap.get(n).getGoldOnPath();
            }
        }
        return higestGold;
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
