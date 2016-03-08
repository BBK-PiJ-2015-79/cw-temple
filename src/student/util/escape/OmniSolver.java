package student.util.escape;

import game.Node;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by chris on 27/02/2016.
 */
public class OmniSolver implements EscapeSolver {
    private final Node start;
    private final Node end;
    private final Collection<Node> vertices;
    private final int timeLimit;

    public OmniSolver(Node start, Node end, Collection<Node> vertices, int timeLimit) {
        this.start = start;
        this.end = end;
        this.vertices = vertices;
        this.timeLimit = timeLimit;
    }

    @Override
    public EscapePath getPath() {
        EscapePath unOptimisedPath = (new CompositeEscapeSolver(start, end, vertices, timeLimit)).getPath();
        return grabLowHangingFruit(unOptimisedPath, vertices, (timeLimit - unOptimisedPath.getPathLength()));
    }

    private EscapePath grabLowHangingFruit(EscapePath path, Collection<Node> vertices, int timeRemaining) {
        //TODO implement this
        System.out.println("Extra time: " + timeRemaining);
        Deque<Node> pathNodes = new LinkedList<>();
        for(int i = 0; i < path.size(); i++) {
            pathNodes.addLast(path.get(i));
        }
        Set<Node> candidateNodes =  pathNodes.stream()
                .filter(n -> n.getNeighbours().size() > 2).collect(Collectors.toSet());
        Set<EscapePath> potentialPaths = new HashSet<>();
        for(Node n : candidateNodes) {
            for(Node m: n.getNeighbours()) {
                if(!pathNodes.contains(m)) {

                }
            }
        }
        System.out.println(candidateNodes.size());
        return path;
    }
}
