package main;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * @author github.com/butburg (EW) on Okt 2021
 */
public class Puzzle {

    //TODO find better way, maybe use enum
    private static final int UP = 1;
    private static final int DOWN = 2;
    private static final int LEFT = 3;
    private static final int RIGHT = 4;
    private final int numberN;

    private List<Integer> solutionSeq = new ArrayList<>();

    private Node parentNode;
    private Node goal;
    PriorityQueue<Node> liveNodes = new PriorityQueue<>();
    private List<Node> visited = new LinkedList<>();
    private int seenNodes = 0;

    public Puzzle(int[][] inputMatrix, int[][] goalMatrix) {
        this.numberN = inputMatrix[0].length;
        parentNode = new Node(inputMatrix, 0);
        goal = new Node(goalMatrix, 0);
        System.out.println("START");
        printMatrix(parentNode);
        printMatrix(goal);
        System.out.println("----");
    }


    public Node calculate() {
        parentNode.setCostF(0);
        parentNode.setWeight(0);

        liveNodes.clear();
        visited.clear();

        goal.setParent(null);

        liveNodes.add(parentNode);

        while (!liveNodes.isEmpty()) {
            System.out.println("liveNodes: " + liveNodes.size());
            Node current = liveNodes.peek();

            if (current.equals(goal)) {
                System.out.println("found solution" + current.getWeight());
                System.out.println("checked nodes: " + seenNodes);
                return current;
            }
            seenNodes++;
            liveNodes.remove(current);
            visited.add(current);
            findChildren(current);
        }
        return null;
    }

    private void findChildren(Node current) {
        move(UP, current);
        move(DOWN, current);
        move(LEFT, current);
        move(RIGHT, current);
    }

    private void move(int direction, Node current) {
        if (movementValid(direction, current)) {
            System.out.println("Move " + direction + " is valid. (U,D,L,R)" +"for");
            printMatrix(current);
            Node child = createChild(direction, current);

            child.setParent(current);
            child.setWeight(current.getWeight() + 1);
            child.setHeuristicCost(child, goal);


            //TODO beautify code here
            if (!liveNodes.contains(child) && !visited.contains(child)) {
                liveNodes.add(child);
            } else {
                if (liveNodes.contains(child)) {
                    Node foundNode = null;
                    for (Node n : liveNodes) {
                        if (child.equals(n)) {
                            foundNode = n;
                        }
                    }
                    if (child.getWeight() < foundNode.getWeight()) {
                        liveNodes.remove(foundNode);
                        liveNodes.add(child);
                    }

                } else if (visited.contains(child)) {
                    Node foundNode = null;
                    for (Node n : visited) {
                        if (child.equals(n)) {
                            foundNode = n;
                        }
                    }
                    if (child.getWeight() < foundNode.getWeight()) {
                        visited.remove(foundNode);
                        visited.add(child);
                    }
                }
            }
        }
    }

    private boolean movementValid(int direction, Node current) {
        // check if the movement is valid, aka inside the grid
        switch (direction) {
            case UP:
                if (current.getY() - 1 >= 0 && current.getPREVMOVE() != DOWN) return true;
                break;
            case DOWN:
                if (current.getY() + 1 < numberN && current.getPREVMOVE() != UP) return true;
                break;
            case LEFT:
                if (current.getX() - 1 >= 0 && current.getPREVMOVE() != RIGHT) return true;
                break;
            case RIGHT:
                if (current.getX() + 1 < numberN && current.getPREVMOVE() != LEFT) return true;
                break;
        }
        return false;
    }

    private Node createChild(int direction, Node current) {
        // move the empty space with swapping
        int[][] matrix = current.getMatrixCopy();
        int x = current.getX();
        int y = current.getY();

        switch (direction) {
            case UP:
                matrix[y][x] = matrix[y - 1][x];
                matrix[y - 1][x] = 0;
                break;
            case DOWN:
                matrix[y][x] = matrix[y + 1][x];
                matrix[y + 1][x] = 0;
                break;
            case LEFT:
                matrix[y][x] = matrix[y][x - 1];
                matrix[y][x - 1] = 0;
                break;
            case RIGHT:
                matrix[y][x] = matrix[y][x + 1];
                matrix[y][x + 1] = 0;
                break;
        }
        System.out.println("After Move new Child: " + direction);
        Node node = new Node(matrix, direction);
        node.printMatrix();
        return node;
    }


    private void printMatrix(Node node) {
        node.printMatrix();
    }
}