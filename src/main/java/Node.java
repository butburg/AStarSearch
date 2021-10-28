package main.java;

import java.awt.*;
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


    private ArrayList<Node> nodeChildren;

    public Node getNodeParent() {
        return nodeParent;
    }

    private Node nodeParent;
    private double weight; //weight / depth
    private double heuristic; //heuristic
    private double costF; //sum og g(v) and h(v)

    private int PREVMOVE = 0;
    private int numberN;

    public Node(int[][] matrix, int previousMovement) {
        //System.out.println("New Node");
        this.matrix = matrix;
        numberN = matrix.length;
        findBlank(matrix);
        nodeChildren = new ArrayList<>();
        PREVMOVE = previousMovement;
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

    private Point findBlank(int[][] matrix) {
        for (int y = 0; y < numberN; y++) {
            int[] row = matrix[y];
            for (int x = 0; x < row.length; x++) {
                if (matrix[y][x] == 0) {
                    setX(x);
                    setY(y);
                }
            }
        }
        return new Point(x, y);
    }

    public int[][] getMatrixCopy() {
        int[][] copyMatrix = new int[numberN][numberN];
        for (int i = 0; i < numberN; i++)
            for (int j = 0; j < matrix[i].length; j++)
                copyMatrix[i][j] = matrix[i][j];
        return copyMatrix;
    }

    public void setMatrix(int[][] matrix) {
        this.matrix = matrix;
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

    public Node getStateParent() {
        return nodeParent;
    }

    public void setParent(Node nodeParent) {
        this.nodeParent = nodeParent;
    }

    public ArrayList<Node> getStateChildren() {
        return nodeChildren;
    }

    public void setStateChildren(ArrayList<Node> nodeChildren) {
        this.nodeChildren = nodeChildren;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double setHeuristicCost(Node child, Node goal) {
        // calculate heuristic
        double heuristic = 0.0;
        for (int row = 0; row < numberN; row++) {
            for (int col = 0; col < numberN; col++) {
                int[][] childMatrix = child.getMatrixCopy();
                int[][] goalMatrix = goal.getMatrixCopy();
                int find = childMatrix[col][row];

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
        costF = weight + heuristic;
        return this.heuristic = heuristic;
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

    public int getPREVMOVE() {
        return PREVMOVE;
    }

    public void setPREVMOVE(int PREVMOVE) {
        this.PREVMOVE = PREVMOVE;
    }
}
