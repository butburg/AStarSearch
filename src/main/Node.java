package main;

import java.util.ArrayList;

/**
 * @author github.com/butburg (EW) on Okt 2021
 */
public class Node implements Comparable<Node> {
    private int[][] puzzle;
    //empty field location in puzzle;
    private int x;
    private int y;


    private ArrayList<Node> nodeChildren;

    private Node nodeParent;
    private double weight; //weight / depth
    private double heuristic; //heuristic
    private double costF; //sum og g(v) and h(v)

    public Node(int[][] puzzle) {
        this.puzzle = puzzle;
        nodeChildren = new ArrayList<>();
    }

    public int[][] getPuzzle() {
        return puzzle;
    }

    public void setPuzzle(int[][] puzzle) {
        this.puzzle = puzzle;
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

    public double getHeuristic() {
        return heuristic;
    }

    public void setHeuristic(double heuristic) {
        this.heuristic = heuristic;
    }

    public double getCostF() {
        return costF;
    }

    public void setCostF(double costF) {
        this.costF = costF;
    }

    @Override
    public int compareTo(Node o) {
        return Double.compare(this.costF, o.getCostF());
    }

}
