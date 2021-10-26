package main;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * @author github.com/butburg (EW) on Okt 2021
 */
public class Puzzle {
    private Node node;
    private int[][] inputMatrix;
    private int[][] goalMatrix;
    private final int numberN;

    private List<Integer> solutionSeq = new ArrayList<>();
    private List<Integer> priorityQueue = new ArrayList<>();
    private Set<int[][]> visitedNodes = new HashSet<>();
    private int seenNodes = 0;

    public Puzzle(int[][] inputMatrix, int[][] goalMatrix) {
        this.numberN = inputMatrix[0].length;
        this.goalMatrix = goalMatrix;
        this.inputMatrix = inputMatrix;

    }

    public int[][] calculate() {
        node = new Node(inputMatrix);
        node.setF(0);

        PriorityQueue<Node> nodePriorityQueue = new PriorityQueue<>();
        Node currentNode = node;

        while (!currentNode.getPuzzle().equals(goalMatrix)) {
            visitedNodes.add(currentNode.getPuzzle());
            List<int[][]> nodeNachfolger = new ArrayList<>();
            for (int[][] matrix : nodeNachfolger){
                if(visitedNodes.contains(matrix)){
                    continue;
                }

            }

        }


        return new int[0][0];

    }

    public double h(Point v, Point w) {
        return Math.abs(v.getX() - v.getY()) + Math.abs(w.getX() - w.getY());
    }
}
