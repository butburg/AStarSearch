package main.java;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * @author github.com/butburg (EW) on Okt 2021
 */
public class Puzzle {

    private final int NUMBER_N;
    private final int LIMIT = 3000;
    private int seenNodes = 0;
    private String moveSeq;
    private Node parentNode;
    private Node goal;
    PriorityQueue<Node> liveNodes = new PriorityQueue<>();
    private List<Node> visited = new LinkedList<>();

    public Puzzle(int[][] inputMatrix, int[][] goalMatrix) {
        this.NUMBER_N = inputMatrix[0].length;
        parentNode = new Node(inputMatrix, Movement.NONE);
        goal = new Node(goalMatrix, Movement.NONE);
    }

    public Node calculate() {
        //reset everything
        parentNode.setCostF(0);
        parentNode.setDepth(0);
        liveNodes.clear();
        visited.clear();
        goal.setParent(null);

        liveNodes.add(parentNode);

        Node current;
        while (!liveNodes.isEmpty() && seenNodes < LIMIT) {
            System.out.println("actual count of liveNodes: " + liveNodes.size());
            //get the node with lowest cost
            current = liveNodes.peek();

            //found the solution?
            if (current.equals(goal)) {
                moveSeq = tracePath(current, "");
                return current;
            }

            //otherwise put it in the list of seen nodes
            liveNodes.remove(current);
            visited.add(current);
            findChildren(current);
        }
        return null;
    }

    private String tracePath(Node current, String moves) {
        switch (current.getPrevMove()) {
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
        //print the movement of the blanket
        System.out.println(current.printMatrix());
        return tracePath(current.getParent(), moves);
    }

    private void findChildren(Node current) {
        move(Movement.UP, current);
        move(Movement.DOWN, current);
        move(Movement.LEFT, current);
        move(Movement.RIGHT, current);
    }

    private void move(Movement direction, Node current) {
        if (movementValid(direction, current)) {
            Node child = createChild(direction, current);

            child.setParent(current);
            child.setDepth(current.getDepth() + 1);
            child.setHeuristicCost(goal);
            child.calculateF();

            if (!liveNodes.contains(child) && !visited.contains(child)) {
                liveNodes.add(child);
                seenNodes++;
            } else {
                if (liveNodes.contains(child)) {
                    updateListWithLowerCostNode(child, liveNodes);
                } else if (visited.contains(child)) {
                    updateListWithLowerCostNode(child, visited);
                }
            }
        }
    }

    private void updateListWithLowerCostNode(Node child, Collection<Node> list) {
        Node foundNode = null;
        for (Node n : list) {
            if (child.equals(n)) {
                foundNode = n;
            }
        }
        if (child.getDepth() < foundNode.getDepth()) {
            list.remove(foundNode);
            list.add(child);
        }
    }

    private boolean movementValid(Movement direction, Node current) {
        // check if the movement is valid, aka inside the grid
        switch (direction) {
            case UP:
                if (current.getY() - 1 >= 0 && current.getPrevMove() != Movement.DOWN) return true;
                break;
            case DOWN:
                if (current.getY() + 1 < NUMBER_N && current.getPrevMove() != Movement.UP) return true;
                break;
            case LEFT:
                if (current.getX() - 1 >= 0 && current.getPrevMove() != Movement.RIGHT) return true;
                break;
            case RIGHT:
                if (current.getX() + 1 < NUMBER_N && current.getPrevMove() != Movement.LEFT) return true;
                break;
        }
        return false;
    }

    private Node createChild(Movement direction, Node current) {
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

    public int getLIMIT() {
        return LIMIT;
    }

    public int getSeenNodes() {
        return seenNodes;
    }

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
}