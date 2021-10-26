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

    private Node nodeParent;

    private ArrayList<Node> nodeChildren;

    private double g; //weight
    private double h; //heuristic
    private double f; //sum og g(v) and h(v)

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

    public void setStateParent(Node nodeParent) {
        this.nodeParent = nodeParent;
    }

    public ArrayList<Node> getStateChildren() {
        return nodeChildren;
    }

    public void setStateChildren(ArrayList<Node> nodeChildren) {
        this.nodeChildren = nodeChildren;
    }

    public double getG() {
        return g;
    }

    public void setG(double g) {
        this.g = g;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    public double getF() {
        return f;
    }

    public void setF(double f) {
        this.f = f;
    }

    @Override
    public int compareTo(Node o) {
        return Double.compare(this.f, o.getF());
    }

}
