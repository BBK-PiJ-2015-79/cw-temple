package student.util.escape;

import game.Node;
import static student.util.escape.MappingStatus.*;

/**
 * Created by chris on 10/02/2016.
 */
public class EscapeNodeStatus {
    private long nodeId;
    private MappingStatus nodeStatus;
    private int searchDepth;
    private int pathDistance;
    private int goldOnPath;
    private Node predecessor;
    private Node successor;

    public EscapeNodeStatus(long id) {
        nodeId = id;
        nodeStatus = UNMAPPED;
        searchDepth = -1;
        pathDistance = -1;
        predecessor = null;
        successor = null;
    }

    public long getNodeId() {
        return nodeId;
    }

    public MappingStatus getNodeStatus() {
        return nodeStatus;
    }

    public void setNodeStatus(MappingStatus nodeStatus) {
        this.nodeStatus = nodeStatus;
    }

    public int getSearchDepth() {
        return searchDepth;
    }

    public void setSearchDepth(int searchDepth) {
        this.searchDepth = searchDepth;
    }

    public Node getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(Node predecessor) {
        this.predecessor = predecessor;
    }

    public int getPathDistance() {
        return pathDistance;
    }

    public void setPathDistance(int pathDistance) {
        this.pathDistance = pathDistance;
    }


    public int getGoldOnPath() {
        return goldOnPath;
    }

    public void setGoldOnPath(int goldOnPath) {
        this.goldOnPath = goldOnPath;
    }

}
