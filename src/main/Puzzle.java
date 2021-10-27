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
        parentNode = new Node(inputMatrix);
        goal = new Node(goalMatrix);
    }

    public Node calculate() {
        parentNode.setCostF(0);
        parentNode.setWeight(0);

        liveNodes.clear();
        visited.clear();

        goal.setParent(null);

        liveNodes.add(parentNode);

        while (!liveNodes.isEmpty()) {

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
            Node child = createChild(direction, current);

            child.setWeight(current.getWeight() + 1);
            child.setParent(current);
            child.setHeuristic(getHeuristicCost(child, goal));


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
        //TODO check if the movement is valid, aka inside the grid
        return true;
    }

    private Node createChild(int direction, Node current) {
        //TODO move the empty space with swapping
        int[][] switchedMatrix = {};
        return new Node(switchedMatrix);
    }

    private double getHeuristicCost(Node child, Node goal) {
        //TODO calculate heuristic
        //Math.abs(v.getX() - v.getY()) + Math.abs(w.getX() - w.getY());
        return 0;
    }
}