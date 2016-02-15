package student;

import game.EscapeState;
import game.Node;

import java.util.*;
import java.util.stream.Collectors;

import static student.MappingStatus.MAPPED;
import static student.MappingStatus.PENDING;
import static student.MappingStatus.UNMAPPED;

/**
 * Created by chris on 15/02/2016.
 */
public class SmarterEscapeSolver implements EscapeSolver {
    HashMap<Node, EscapeNodeStatus> escapeTreeMap;
    HashMap<Node, EscapeNodeStatus> reverseTreeMap;
    LinkedList<Node> queue;
    int escapeTime;

    @Override
    public LinkedList<Node> getPath(Node start, Node end, EscapeState state) {
        escapeTime = state.getTimeRemaining(); // yuck

        escapeTreeMap = new HashMap<>();
        reverseTreeMap = new HashMap<>();
        queue = new LinkedList<>();
        Collection<Node> vertices = state.getVertices();

        setupSearchMap(escapeTreeMap, vertices);
        constructTreeMap(escapeTreeMap, start, end);
        setupSearchMap(reverseTreeMap, vertices);
        constructTreeMap(reverseTreeMap, end, start);

        LinkedList<Node> returnPath;
        returnPath = findAPath(start, end);
        return returnPath;
    }

    //Ugh ugh ugh ugh, YUCK! Basically using global variables. It feels wrong.
    private void setupSearchMap(HashMap<Node, EscapeNodeStatus> searchTreeMap, Collection<Node> vertices) {
        for(Node node : vertices) {
            //System.out.println("Hello!");
            //System.out.println(node);
            searchTreeMap.put(node, new EscapeNodeStatus(node.getId()));
        }
    }

    private void constructTreeMap(HashMap<Node, EscapeNodeStatus> searchTreeMap, Node start, Node end) {
        queue = new LinkedList<>();
        queue.addLast(start);
        searchTreeMap.get(start).setNodeStatus(PENDING);
        searchTreeMap.get(start).setSearchDepth(0);
        searchTreeMap.get(start).setPathDistance(0);
        searchTreeMap.get(start).setGoldOnPath(0);
        //Do BFS
        while(!queue.isEmpty()) {
            //System.out.println(queue);
            Node currentNode = queue.removeFirst();
            // Get the next nodes to add to the queue
            Set<Node> currentUnvisitedNeighbours = currentNode.getNeighbours().stream().filter(n -> {
                //statement style to make more readable
                return searchTreeMap.get(n).getNodeStatus() == UNMAPPED;
            }).collect(Collectors.toSet());
            for(Node node : currentUnvisitedNeighbours) {
                int previousGold;
                previousGold = searchTreeMap.get(currentNode).getGoldOnPath();
                searchTreeMap.get(node).setPredecessor(currentNode);
                searchTreeMap.get(node).setNodeStatus(PENDING);
                searchTreeMap.get(node).setSearchDepth(searchTreeMap.get(currentNode).getSearchDepth() + 1);
                searchTreeMap.get(node).setPathDistance(searchTreeMap.get(currentNode).getPathDistance()
                        + currentNode.getEdge(node).length());
                searchTreeMap.get(node).setGoldOnPath(previousGold + node.getTile().getGold());
                queue.add(node);
            }
            searchTreeMap.get(currentNode).setNodeStatus(MAPPED);
            queue.sort((n1,n2) -> searchTreeMap.get(n1).getPathDistance() - searchTreeMap.get(n2).getPathDistance());
        }
    }

    private LinkedList<Node> findAPath(Node start, Node end) {
        Node richestPathNode = null;
        int pathLength = 0;
        LinkedList<Node> allNodesSorted = new LinkedList<>();
        Set<Node> allNodes;
        allNodes = escapeTreeMap.keySet();
        for(Node n : allNodes) {
            allNodesSorted.add(n);
        }
        allNodesSorted.sort((n1, n2) -> escapeTreeMap.get(n2).getGoldOnPath() - escapeTreeMap.get(n1).getGoldOnPath());
        System.out.println("Richest path found: " + escapeTreeMap.get(allNodesSorted.getFirst()).getGoldOnPath());
        boolean pathFound = false;
        while(!pathFound) {
            richestPathNode = allNodesSorted.removeFirst();
            pathLength = escapeTreeMap.get(richestPathNode).getPathDistance()
                    + reverseTreeMap.get(richestPathNode).getPathDistance();
            if(pathLength < escapeTime) {
                pathFound = true;
            }
            if(allNodesSorted.size() == 0) break;
        }
        if(!pathFound) return new LinkedList<>(); // what to do about this?, no path found.
        LinkedList<Node> returnPath = new LinkedList<>();
        Node currentNode = richestPathNode;
        returnPath.addLast(currentNode);
        while(reverseTreeMap.get(currentNode).getSearchDepth() > 0) {
            Node nextNode = reverseTreeMap.get(currentNode).getPredecessor();
            returnPath.addLast(nextNode);
            currentNode = nextNode;
        }
        currentNode = richestPathNode;
        while(escapeTreeMap.get(currentNode).getSearchDepth() > 0) {
            Node nextNode = escapeTreeMap.get(currentNode).getPredecessor();
            returnPath.addFirst(nextNode);
            currentNode = nextNode;
        }
        return returnPath;
    }

}
