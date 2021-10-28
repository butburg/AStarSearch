package main.java;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * @author github.com/butburg (EW) on Okt 2021
 */
public class Puzzle {


    //TODO find better way, maybe use enum
    private final int UP = 1;
    private final int DOWN = 2;
    private final int LEFT = 3;
    private final int RIGHT = 4;
    private final int NUMBER_N;

    public int getLIMIT() {
        return LIMIT;
    }

    private final int LIMIT = 3000;

    private String moveSeq;

    private Node parentNode;
    private Node goal;
    PriorityQueue<Node> liveNodes = new PriorityQueue<>();
    private List<Node> visited = new LinkedList<>();

    public int getSeenNodes() {
        return seenNodes;
    }

    private int seenNodes = 0;

    public int getNUMBER_N() {
        return NUMBER_N;
    }

    public String getMoveSeq() {
        return moveSeq;
    }

    public Node getParentNode() {
        return parentNode;
    }

    public Node getGoal() {
        return goal;
    }

    public PriorityQueue<Node> getLiveNodes() {
        return liveNodes;
    }

    public List<Node> getVisited() {
        return visited;
    }

    public Puzzle(int[][] inputMatrix, int[][] goalMatrix) {
        this.NUMBER_N = inputMatrix[0].length;
        parentNode = new Node(inputMatrix, 0);
        goal = new Node(goalMatrix, 0);
        System.out.println("-START-");
        System.out.println(printMatrix(parentNode));
        System.out.print(printMatrix(goal));
        System.out.println("------");
    }


    public Node calculate() {
        parentNode.setCostF(0);
        parentNode.setWeight(0);

        liveNodes.clear();
        visited.clear();

        goal.setParent(null);

        liveNodes.add(parentNode);
        Node current = null;
        while (!liveNodes.isEmpty() && seenNodes < LIMIT) {
            System.out.println("liveNodes: " + liveNodes.size());
            current = liveNodes.peek();
            seenNodes++;
            if (current.equals(goal)) {
                System.out.println("found solution" + current.getWeight());
                System.out.println("checked nodes: " + seenNodes);
                moveSeq = tracePath(current, "");
                return current;
            }

            liveNodes.remove(current);
            visited.add(current);
            findChildren(current);
        }
        return null;
    }


    private String tracePath(Node current, String moves) {
        switch (current.getPREVMOVE()) {
            case UP:
                moves = "U" + moves;
                break;
            case DOWN:
                moves = "D" + moves;
                break;
            case LEFT:
                moves = "L" + moves;
                break;
            case RIGHT:
                moves = "R" + moves;
                break;
            default:
                return moves;
        }
        System.out.println(current.printMatrix());
        return tracePath(current.getNodeParent(), moves);
    }

    private void findChildren(Node current) {
        move(UP, current);
        move(DOWN, current);
        move(LEFT, current);
        move(RIGHT, current);
    }

    private void move(int direction, Node current) {
        if (movementValid(direction, current)) {
            //System.out.println("Move " + direction + " is valid. (U,D,L,R)" + "for");
            //printMatrix(current);
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
                if (current.getY() + 1 < NUMBER_N && current.getPREVMOVE() != UP) return true;
                break;
            case LEFT:
                if (current.getX() - 1 >= 0 && current.getPREVMOVE() != RIGHT) return true;
                break;
            case RIGHT:
                if (current.getX() + 1 < NUMBER_N && current.getPREVMOVE() != LEFT) return true;
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
        //System.out.println("After Move new Child: " + direction);
        Node node = new Node(matrix, direction);
        //node.printMatrix();
        return node;
    }


    private String printMatrix(Node node) {
        return node.printMatrix();
    }

    public String getMoves() {
        return moveSeq;
    }
}