package student.util.escape;

import game.Node;

import java.util.*;

/**
 * //TODO documentation
 * Created by chris on 24/02/2016.
 */
public class CompositeEscapeSolver implements EscapeSolver {

    private Node start;
    private Node end;
    private Collection<Node> vertices;
    private int timeLimit;

    private SearchMap fastMap;
    private SearchMap reverseFastMap;
    private SearchMap greedyMap;
    private SearchMap reverseGreedyMap;
    private SearchMap zenMap;
    private SearchMap reverseZenMap;

    private SearchStrategy sortingStrategy;

    private Set<EscapePath> solutionSet;

    public CompositeEscapeSolver(Node start, Node end, Collection<Node> vertices, int timeLimit) {
        this.sortingStrategy = new GreedySearchStrategy(); // shame about the name mismatch
        this.start = start;
        this.end = end;
        this.vertices = vertices;
        this.timeLimit = timeLimit;
        setupSearchMaps();
        this.solutionSet = new HashSet<>();
        generateSolutions();
        this.solutionSet.add((new FastEscapeSolver(start, end, vertices, timeLimit)).getPath()); // safety net
    }

    private void setupSearchMaps() {
        fastMap = new SearchMap(start, end, vertices, new FastSearchStrategy());
        reverseFastMap = new SearchMap(end, start, vertices, new FastSearchStrategy());
        greedyMap = new SearchMap(start, end, vertices, new GreedySearchStrategy());
        reverseGreedyMap = new SearchMap(end, start, vertices, new GreedySearchStrategy());
        zenMap = new SearchMap(start, end, vertices, new ZenSearchStrategy());
        reverseZenMap = new SearchMap(end, start, vertices, new ZenSearchStrategy());
    }

    private void generateSolutions() {
        //TODO
        solutionSet.add(getCompositePath(fastMap, reverseFastMap));
        solutionSet.add(getCompositePath(fastMap, reverseGreedyMap));
        solutionSet.add(getCompositePath(fastMap, reverseZenMap));

        solutionSet.add(getCompositePath(greedyMap, reverseFastMap));
        solutionSet.add(getCompositePath(greedyMap, reverseGreedyMap));
        solutionSet.add(getCompositePath(greedyMap, reverseZenMap));

        solutionSet.add(getCompositePath(zenMap, reverseFastMap));
        solutionSet.add(getCompositePath(zenMap, reverseGreedyMap));
        solutionSet.add(getCompositePath(zenMap, reverseZenMap));
    }

    private EscapePath getCompositePath(SearchMap someSearchMap, SearchMap someReverseSearchMap) {
        int candidatePathLength;
        Node candidateMidPoint = null;
        boolean pathFound = false;
        LinkedList<Node> sortedNodeList = new LinkedList<>();
        for(Node n : vertices) {
            sortedNodeList.add(n);
        }
        sortedNodeList.sort((n1, n2) -> sortingStrategy
                .compare(someSearchMap.getNodeStatus(n1), someSearchMap.getNodeStatus(n2)));
        while(!pathFound) {
            candidateMidPoint = sortedNodeList.removeFirst();
            candidatePathLength = someSearchMap.getPathTo(candidateMidPoint).getPathLength() +
                    someReverseSearchMap.getPathTo(candidateMidPoint).getPathLength();
            if(candidatePathLength <= timeLimit) {
                pathFound = true;
            }
            if(sortedNodeList.size() == 0) break;
        }
        EscapePath solution = new EscapePath();
        if(pathFound) {
            solution = someSearchMap.getPathTo(candidateMidPoint)
                    .concatenatePath(someReverseSearchMap.getPathTo(candidateMidPoint).getReversePath());
        }
        return solution;
    }

    @Override
    public EscapePath getPath() {
        return solutionSet.stream().max((l1, l2) -> l1.getGoldOnPath() - l2.getGoldOnPath()).get();
    }
}
