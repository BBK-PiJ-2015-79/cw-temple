package student.util.escape;

import game.Node;

import java.util.*;

/**
 * This escape solver uses a combination of search strategies to try and find a path through
 * the EscapeState which is better than any of the individual strategies by themselves.
 *
 * The CompositeEscapeSolver starts by setting up SearchMaps both from the currentState to the
 * exit, but also from the exit to the currentState, taking advantage of the fact that paths are
 * reversible.
 *
 * The CompositeEscapeSolver then looks for the richest path in each SearchMap <em>regardless of where
 * this path ends up.</em> The CompositeEscapeSolver then looks for a path from the exit to the end
 * point of the richest path, and glues these together to form a composite path from the start point
 * to the exit.
 *
 * Created by chris on 24/02/2016.
 *
 * @author Chris Grocott
 */
//TODO cleanup fields (what can be final, what can be removed), refactor repeating sections
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
        //TODO there has to be a way to DRY this out.
        fastMap = new SearchMap(start, end, vertices, new FastSearchStrategy());
        reverseFastMap = new SearchMap(end, start, vertices, new FastSearchStrategy());
        greedyMap = new SearchMap(start, end, vertices, new GreedySearchStrategy());
        reverseGreedyMap = new SearchMap(end, start, vertices, new GreedySearchStrategy());
        zenMap = new SearchMap(start, end, vertices, new ZenSearchStrategy());
        reverseZenMap = new SearchMap(end, start, vertices, new ZenSearchStrategy());
    }

    private void generateSolutions() {
        //TODO there has to be a way to DRY this out.
        solutionSet.add(getCompositePath(fastMap, reverseFastMap));
        solutionSet.add(getCompositePath(fastMap, reverseGreedyMap));
        solutionSet.add(getCompositePath(fastMap, reverseZenMap));

        solutionSet.add(getCompositePath(reverseFastMap, fastMap).getReversePath());
        solutionSet.add(getCompositePath(reverseGreedyMap, fastMap).getReversePath());
        solutionSet.add(getCompositePath(reverseZenMap, fastMap).getReversePath());

        solutionSet.add(getCompositePath(greedyMap, reverseFastMap));
        solutionSet.add(getCompositePath(greedyMap, reverseGreedyMap));
        solutionSet.add(getCompositePath(greedyMap, reverseZenMap));

        solutionSet.add(getCompositePath(reverseFastMap, greedyMap).getReversePath());
        solutionSet.add(getCompositePath(reverseGreedyMap, greedyMap).getReversePath());
        solutionSet.add(getCompositePath(reverseZenMap, greedyMap).getReversePath());

        solutionSet.add(getCompositePath(zenMap, reverseFastMap));
        solutionSet.add(getCompositePath(zenMap, reverseGreedyMap));
        solutionSet.add(getCompositePath(zenMap, reverseZenMap));

        solutionSet.add(getCompositePath(reverseFastMap, zenMap).getReversePath());
        solutionSet.add(getCompositePath(reverseGreedyMap, zenMap).getReversePath());
        solutionSet.add(getCompositePath(reverseZenMap, zenMap).getReversePath());
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
