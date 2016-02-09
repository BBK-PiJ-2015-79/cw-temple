package student;

import game.EscapeState;
import game.ExplorationState;
import game.Node;
import game.NodeStatus;

import java.util.*;
import java.util.stream.Collectors;

public class Explorer {
    // instance variables used in explore phase
    private Random rand = new Random();
    private Collection<Long> unvisitedNeighbours; //use to choose where to go


    /**
     * Explore the cavern, trying to find the
     * orb in as few steps as possible. Once you find the
     * orb, you must return from the function in order to pick
     * it up. If you continue to move after finding the orb rather
     * than returning, it will not count.
     * If you return from this function while not standing on top of the orb,
     * it will count as a failure.
     * <p>
     * There is no limit to how many steps you can take, but you will receive
     * a score bonus multiplier for finding the orb in fewer steps.
     * <p>
     * At every step, you only know your current tile's ID and the ID of all
     * open neighbor tiles, as well as the distance to the orb at each of these tiles
     * (ignoring walls and obstacles).
     * <p>
     * In order to get information about the current state, use functions
     * getCurrentLocation(), getNeighbours(), and getDistanceToTarget() in ExplorationState.
     * You know you are standing on the orb when getDistanceToTarget() is 0.
     * <p>
     * Use function moveTo(long id) in ExplorationState to move to a neighboring
     * tile by its ID. Doing this will change state to reflect your new position.
     * <p>
     * A suggested first implementation that will always find the orb, but likely won't
     * receive a large bonus multiplier, is a depth-first search.
     *
     * @param state the information available at the current state
     */
    public void explore(ExplorationState state) {
        //TODO : Look for further optimisation, figure out why unvisitedNeighbours had to be a field
        //TODO : for goodness sake, clean this code up!
        Stack<Long> breadCrumbs = new Stack<>(); // to help with backtracking
        Collection<Long> visitedSquares = new HashSet<>(); // give priority to unvisited squares
        //Collection<Long> unvisitedNeighbours; //use to choose where to go
        long next; //the id of the next square to move to

        Collection<NodeStatus> neighbours; // immediate neighbours

        while(state.getDistanceToTarget() > 0) {
            //let's hunt some orb
            if(!visitedSquares.contains(state.getCurrentLocation())) {
                visitedSquares.add(state.getCurrentLocation());
            }
            unvisitedNeighbours = new HashSet<>();
            neighbours = state.getNeighbours();
            neighbours.stream().filter(n -> (!visitedSquares.contains(n.getId())))
                    .forEach(n -> unvisitedNeighbours.add(n.getId()));
            if(unvisitedNeighbours.size() == 0) {
                //System.out.println(breadCrumbs); //debug
                //System.out.println("backtracking"); //debug
                next = breadCrumbs.pop();
            }
            else {
                next = neighbours.stream().filter(n -> (!visitedSquares.contains(n.getId())))
                        .min((n1,n2) -> (n1.getDistanceToTarget() - n2.getDistanceToTarget())).get().getId();
                //next = unvisitedNeighbours.stream().findFirst().get();
                breadCrumbs.push(state.getCurrentLocation());
            }
            //System.out.println(breadCrumbs);
            state.moveTo(next);
        }
    }

    /**
     * Escape from the cavern before the ceiling collapses, trying to collect as much
     * gold as possible along the way. Your solution must ALWAYS escape before time runs
     * out, and this should be prioritized above collecting gold.
     * <p>
     * You now have access to the entire underlying graph, which can be accessed through EscapeState.
     * getCurrentNode() and getExit() will return you Node objects of interest, and getVertices()
     * will return a collection of all nodes on the graph.
     * <p>
     * Note that time is measured entirely in the number of steps taken, and for each step
     * the time remaining is decremented by the weight of the edge taken. You can use
     * getTimeRemaining() to get the time still remaining, pickUpGold() to pick up any gold
     * on your current tile (this will fail if no such gold exists), and moveTo() to move
     * to a destination node adjacent to your current node.
     * <p>
     * You must return from this function while standing at the exit. Failing to do so before time
     * runs out or returning from the wrong location will be considered a failed run.
     * <p>
     * You will always have enough time to escape using the shortest path from the starting
     * position to the exit, although this will not collect much gold.
     *
     * @param state the information available at the current state
     */
    public void escape(EscapeState state) {
        //TODO: Escape from the cavern before time runs out
        LinkedList<Node> solution;
        Node currentNode = state.getCurrentNode();
        Node exitNode = state.getExit();
        EscapeSolver solver = new DFSEscapeSolver();
        solution = solver.getPath(currentNode, exitNode, state);
        followPath(solution, state);
    }

    private void followPath(LinkedList<Node> solution, EscapeState state) {
        solution.removeFirst(); //because I'm already there?
        while(solution.size() > 0) {
            try {
                state.pickUpGold();
            }
            catch(IllegalStateException e) {
                //System.out.println("No gold here!");
            }
            state.moveTo(solution.removeFirst());
        }
        try {
            state.pickUpGold();
        }
        catch(IllegalStateException e) {
            //System.out.println("No gold here!");
        }
    }
}
