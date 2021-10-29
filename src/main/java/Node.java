package main.java;


import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author github.com/butburg (EW) on Okt 2021
 */
public class Node implements Comparable<Node> {
    private int[][] matrix;
    //empty field location in puzzle;
    private int x;
    private int y;

    //list of all new states/moved nodes from this node
    private ArrayList<Node> nodeChildren;
    private Node nodeParent;
    private double depth; //weight / depth
    private double heuristic; //heuristic
    private double costF; //sum og g(v) and h(v)
    private Movement prevMove;
    private int numberN;

    public Node(int[][] matrix, Movement previousMovement) {
        this.matrix = matrix;
        this.numberN = matrix.length;
        setBlankPosition(matrix);
        this.nodeChildren = new ArrayList<>();
        this.prevMove = previousMovement;
    }

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

    public double calculateF() {
        return costF = depth + heuristic;
    }

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

    @Override
    public boolean equals(Object obj) {
        Node testNode = (Node) obj;
        if (Arrays.deepEquals(testNode.getMatrixCopy(), this.getMatrixCopy()))
            return true;
        else
            return false;
    }

    @Override
    public int compareTo(Node o) {
        return Double.compare(this.costF, o.getCostF());
    }
}
