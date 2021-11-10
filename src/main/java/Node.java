package main.java;


import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author github.com/butburg (EW) on Okt 2021
 * <p>
 * A node or state is representing a specific state of the sliding puzzle game. While u move the blank tile,
 * the field is changeing it state. Theses states are represented by one of these nodes. Additionally the node
 * stores some informations about the actual cost of that state. This consists of the depth(how many parents
 * recusrively the node has) and some heuristic costs (how far ist the actual state from the goal state). Also
 * every node knows his parent node, so when a solution is found, the path to that solution can be backtraced.
 */
public class Node implements Comparable<Node> {
    /**
     * the matrix represents the actual field. All the positions of the tiles and the blanke tile.
     */
    private int[][] matrix;
    /**
     * Empty field location in puzzle. The blank tile. Coordinate x.
     */
    private int x;
    /**
     * Empty field location in puzzle. The blank tile. Coordinate y.
     */
    private int y;

    /**
     * list of all new states/moved nodes from this node
     */
    private ArrayList<Node> nodeChildren;

    /**
     * The ancestor. The node this node origins from.
     */
    private Node nodeParent;
    /**
     * How deep in the chain of parent nodes.
     */
    private double depth; //weight / depth
    /**
     * The heuristic cost, in compare to the goal node. Lower when its close to the goal.
     */
    private double heuristic; //heuristic
    /**
     * The sum of the costs g(v)/depth and h(v)/heuristic.
     */
    private double costF;
    /**
     * The movement that was done to reach this state. Don't move into the opposite of it otherwise it will be
     * like the parent. We don't need doubled states.
     */
    private Movement prevMove;
    /**
     * The dimension of the matrix/field size.
     */
    private int numberN;

    /**
     * Will initialize a new node. Will also read the position of the blank tile and store it as coordinates.
     *
     * @param matrix           the actual field the node will represent.
     * @param previousMovement the movement, that was done to reach this new node.
     */
    public Node(int[][] matrix, Movement previousMovement) {
        this.matrix = matrix;
        this.numberN = matrix.length;
        setBlankPosition(matrix);
        this.nodeChildren = new ArrayList<>();
        this.prevMove = previousMovement;
    }

    /**
     * stores the actual position of the blank node. Helps other methods to access that position easily.
     *
     * @param matrix the actual matrix the postion should be read from.
     */
    private void setBlankPosition(int[][] matrix) {
        for (int y = 0; y < numberN; y++) {
            int[] row = matrix[y];
            for (int x = 0; x < row.length; x++) {
                if (matrix[y][x] == 0) {
                    setX(x);
                    setY(y);
                }
            }
        }
    }

    /**
     * Calculates the sum of the costs g(v)/depth and h(v)/heuristic
     *
     * @return the cost F as double
     */
    public double calculateF() {
        return costF = depth + heuristic;
    }

    /**
     * prints the matrix game field of this node/state
     *
     * @return
     */
    public String printMatrix() {
        StringBuilder matrixPrint = new StringBuilder();
        for (int[] row : matrix) {
            for (int digit : row) {
                matrixPrint.append(digit + " ");
            }
            matrixPrint.append("\n");
        }
        return matrixPrint.toString();
    }

    /**
     * calculates and sets the heuristic cost. Will calculate that cost based on how far every tile is from its
     * final position. The distance will be summed up from every tile, this will be the result.
     *
     * @param goal where the tiles should be from this nodes in an optimal situation
     * @return the cost representing the distance/cost to reach the goal
     */
    public double setHeuristicCost(Node goal) {
        // calculate heuristic
        double heuristic = 0.0;
        for (int row = 0; row < numberN; row++) {
            for (int col = 0; col < numberN; col++) {

                int[][] goalMatrix = goal.getMatrixCopy();
                int find = matrix[col][row];

                if (find != 0) {
                    for (int rowG = 0; rowG < numberN; rowG++) {
                        for (int colG = 0; colG < numberN; colG++) {
                            if (goalMatrix[colG][rowG] == find) {
                                heuristic += Math.abs(rowG - row) + Math.abs(colG - col);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return this.heuristic = heuristic;
    }

    public int[][] getMatrixCopy() {
        int[][] copyMatrix = new int[numberN][numberN];
        for (int i = 0; i < numberN; i++)
            for (int j = 0; j < matrix[i].length; j++)
                copyMatrix[i][j] = matrix[i][j];
        return copyMatrix;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setParent(Node nodeParent) {
        this.nodeParent = nodeParent;
    }

    public Node getParent() {
        return nodeParent;
    }

    public ArrayList<Node> getStateChildren() {
        return nodeChildren;
    }

    public double getDepth() {
        return depth;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }

    public double getHeuristic() {
        return heuristic;
    }

    public double getCostF() {
        return costF;
    }

    public void setCostF(double costF) {
        this.costF = costF;
    }

    public Movement getPrevMove() {
        return prevMove;
    }

    /**
     * The method equals will return true, if the matrices are identical. The costs and other things are not considered.
     */
    @Override
    public boolean equals(Object obj) {
        Node testNode = (Node) obj;
        if (Arrays.deepEquals(testNode.getMatrixCopy(), this.getMatrixCopy()))
            return true;
        else
            return false;
    }

    /**
     * The nodes will be compared by there cost F. So that we can insert the nodes in a priority queue for example.
     *
     * @param o another node to campere this one to
     * @return -1 if this cost is lower than the other cost, 0 equal and 1 if this cost is higher
     */
    @Override
    public int compareTo(Node o) {
        return Double.compare(this.costF, o.getCostF());
    }
}
