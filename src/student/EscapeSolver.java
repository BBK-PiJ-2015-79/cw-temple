package student;

/**
 * Interface for escape solvers, added to make swapping in algorithms easier
 */
public interface EscapeSolver {
    /**
     * Return an escape path
     */
    EscapePath getPath();
}
