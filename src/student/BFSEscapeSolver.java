package student;

import game.EscapeState;
import game.Node;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by chris on 09/02/2016.
 */
public class BFSEscapeSolver implements EscapeSolver {
    Set<Node> visitedNodes;
    Set<LinkedList<Node>> activeSearches;
    Set<LinkedList<Node>> solutions;
    LinkedList<Node> solution;
    EscapeState eState;

    @Override
    public LinkedList<Node> getPath(Node start, Node end, EscapeState state) {
        eState = state;
        visitedNodes = new HashSet<>();
        activeSearches = new HashSet<>();
        solutions = new HashSet<>();

        visitedNodes.add(start);

        solution = new LinkedList<>();
        LinkedList<Node> initialSearch = new LinkedList<>();
        initialSearch.addLast(start);
        activeSearches.add(initialSearch);

        Set<Node> currentNeighbours;


        while(!activeSearches.isEmpty()) {
            System.out.println("Searching: " + activeSearches.size() + " possibilities");
            Set<LinkedList<Node>> newActiveSearches = new HashSet<>();
            for(LinkedList<Node> currentPath : activeSearches) {
                currentNeighbours = currentPath.getLast().getNeighbours();
                currentNeighbours = currentNeighbours.stream().filter(n -> {
                    //only get unvisited neighbours
                    return !visitedNodes.contains(n);
                }).collect(Collectors.toSet());
                //System.out.println(currentNeighbours.size() + " neighbours!");
                if(currentNeighbours.contains(end)) {
                    System.out.println("Found solution!");
                    currentPath.addLast(end);
                    solution = currentPath;
                    break;
                }
                addSearches(currentPath, currentNeighbours, newActiveSearches);
            }
            activeSearches = newActiveSearches;
        }
        return solution;
    }

    private void addSearches(LinkedList<Node> currentPath, Set<Node> currentNeighbours, Set<LinkedList<Node>> someList) {
        //
        for(Node node : currentNeighbours) {
            LinkedList<Node> newPath = clonePath(currentPath);
            newPath.addLast(node);
            someList.add(newPath);
        }
    }

    private LinkedList<Node> clonePath(LinkedList<Node> path) {
        LinkedList<Node> clonedPath = new LinkedList<>();
        for(int i=0; i < path.size(); i++) {
            clonedPath.add(i, path.get(i));
        }
        return clonedPath;
    }
}
