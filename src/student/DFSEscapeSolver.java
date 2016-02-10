package student;

import game.EscapeState;
import game.Node;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A particularly dumb escape solver. It uses depth first and
 * backtracking and isn't guaranteed to escape in time!
 */
public class DFSEscapeSolver implements EscapeSolver {
    @Override
    public LinkedList<Node> getPath(Node start, Node end, EscapeState state) {
        int timeLimit = state.getTimeRemaining();

        LinkedList<Node> solution = generateSolution(start, end);

        if(getPathLength(solution) > timeLimit) {
            solution = generateSolution(end, start);
            //System.out.println(getPathLength(solution));
            solution = reverseLL(solution);
        }
        if(getPathLength(solution) > timeLimit) {
            System.out.println("CHECK");
        }
        return solution;
    }

    private LinkedList<Node> generateSolution(Node start, Node end) {
        LinkedList<Node> solution = new LinkedList<>();
        Node currentNode = start;
        Node nextNode;
        Set<Node> currentNeighbours;
        Set<Node> unvisitedNeighbours;
        Node exitNode = end;
        Collection<Node> visitedNodes = new HashSet<>();

        while(!currentNode.equals(exitNode)) {

            //System.out.println("Iteration: " + debugIter + " Route: " + routeLength);
            //do a really dumb search
            if(!visitedNodes.contains(currentNode)) {
                visitedNodes.add(currentNode);
            }
            currentNeighbours = currentNode.getNeighbours();
            unvisitedNeighbours = currentNeighbours.stream().filter(n -> !visitedNodes.contains(n))
                    .collect(Collectors.toSet());
            if(unvisitedNeighbours.size() == 0 /*|| routeLength > timeLimit*/) {
                if(unvisitedNeighbours.size() == 0) {
                    //System.out.println("backtracking (no neighbours)");
                }
                try {
                    nextNode = solution.removeLast();
                    //routeLength -= currentNode.getEdge(nextNode).length();
                }
                catch(NoSuchElementException e) {
                    e.printStackTrace();
                    //System.out.println(solution);
                    return null;
                }
            }
            else {
                //nextNode = unvisitedNeighbours.stream().findFirst().get();

                nextNode = unvisitedNeighbours.stream().min((n1, n2) -> {
                    int n1RowDistance = Math.abs(n1.getTile().getRow() - exitNode.getTile().getRow());
                    int n1ColDistance = Math.abs(n1.getTile().getColumn() - exitNode.getTile().getColumn());
                    int n1Distance = n1ColDistance + n1RowDistance;

                    int n2RowDistance = Math.abs(n2.getTile().getRow() - exitNode.getTile().getRow());
                    int n2ColDistance = Math.abs(n2.getTile().getColumn() - exitNode.getTile().getColumn());
                    int n2Distance = n2ColDistance + n2RowDistance;

                    return n1Distance - n2Distance;
                }).get();
                //nextNode = unvisitedNeighbours.stream().findFirst().get();
                //routeLength += currentNode.getEdge(nextNode).length();
                solution.addLast(currentNode);
            }
            currentNode = nextNode;
        }
        solution.addLast(currentNode);
        return solution;
    }

    private int getPathLength(LinkedList<Node> path) {
        // how am I going to do this?
        int pathLen = 0;
        for(int i = 0; i < (path.size() - 1); i++) {
            pathLen += path.get(i).getEdge(path.get(i+1)).length();
        }
        return pathLen;
    }

    private LinkedList<Node> reverseLL(LinkedList<Node> path) {
        LinkedList<Node> reverse = new LinkedList<>();
        for(int i = 0; i < path.size(); i++) {
            reverse.add(0, path.get(i));
        }
        return reverse;
    }
}
