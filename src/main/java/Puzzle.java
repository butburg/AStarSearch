package main.java;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * @author github.com/butburg (EW) on Okt 2021
 *
 * Represents a sliding puzzle with a matrix of 3x3 or 4x4. It uses Nodes that
 * represent different states of positions of the sliding tiles.
 * The puzzle will try to solve that puzzle by shifting the tiles with an empty
 * space, like the real sliding puzzle works.
 *
 */
public class Puzzle {

    /**
     * The number representing the matrix size, for example 3 means a 3x3 matrix
     */
    private final int NUMBER_N;
    /**
     * After LIMIT many States the puzzle will give up and stop trying to
     * find a solution.
     */
    private final int LIMIT = 3000;
    /**
     * Counter for how many nodes/states of the game field have been checked by
     * the programm.
     */
    private int seenNodes = 0;
    /**
     * Stores the sequence of moving the blank tile to achieve the solution.
     * Will be a sequence of chars representing the direction, e.g. "LR": Left, Right
     */
    private String moveSeq;
    /**
     * The ultimate parent node, the start node with the initial matrix from where the programm discovers
     * children nodes.
     */
    private Node parentNode;
    /**
     * The goal node we want to achieve. Compares the childnodes with this one to
     * find the solution.
     */
    private Node goal;
    /**
     * A queue containing all the live nodes. Keeps them in order according to the
     * cost F of the nodes, the lowest at the top.
     */
    PriorityQueue<Node> liveNodes = new PriorityQueue<>();
    /**
     * A list containing all nodes that were live and are not needed anymore, so
     * which were "visited"/ seen by the algorithm.
     */
    private List<Node> visited = new LinkedList<>();

    /**
     * Creates a new instance of the sliding Puzzle.
     * @param inputMatrix The given matrix representing the start positions of the
     *                    tiles from where we start from.
     * @param goalMatrix The matrix representing the state we want to reach.
     */
    public Puzzle(int[][] inputMatrix, int[][] goalMatrix) {
        this.NUMBER_N = inputMatrix[0].length;
        parentNode = new Node(inputMatrix, Movement.NONE);
        goal = new Node(goalMatrix, Movement.NONE);
    }

    /**
     * The algorithm to solve the problem. It will start with the parent node
     * and check first if the actual parent is the goal state. Otherwise it will
     * find children states with the help of moving the blank tile in possible
     * directions. It will find children nodes until it founds the goal node or the
     * LIMIT of seen nodes is reached.
     * @return the goal node if founded or null if not founded.
     */
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
            //get the node with lowest cost, its at the top of the queue
            current = liveNodes.peek();

            //found the solution?
            if (current.equals(goal)) {
                //find the path, the movements fulfilled to reach that goal
                moveSeq = tracePath(current, "");
                return current;
            }

            //otherwise move it in the list of seen nodes
            liveNodes.remove(current);
            visited.add(current);
            //find the children states with the help of moving the blank tile
            findChildren(current);
        }
        return null;
    }

    /**
     * Finds the way / the movements that were made to reach the goal. Will
     * use a recursion and call from the goal state the parents till there is
     * no parent left, the start node is reached.
     * @param current expects the goal node, from where it will find the parent
     * @param moves the founded movements, will be empty at the beginning.
     * @return the complete sequence of chars like U,D,R,L as a String
     */
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
        //print the movement of the blank tile
        System.out.println(current.printMatrix());
        return tracePath(current.getParent(), moves);
    }

    /**
     * Will call the method to move the blank tile in all directions
     * @param current the node/state from where it moves the tiles
     */
    private void findChildren(Node current) {
        move(Movement.UP, current);
        move(Movement.DOWN, current);
        move(Movement.LEFT, current);
        move(Movement.RIGHT, current);
    }

    /**
     * Will try to fulfill the called movement. Only when a switch is possible and
     * the blank tile moves not out of the matrix or moves in a direction where it
     * comes from, it will actually do the movement and put that new state/child
     * into the list of living nodes. If that state was seen already, it will only
     * update the cost of that node with the lower one cost of them both.
     * @param direction The direction the tile should move, up, down, left or right.
     * @param current The current node/state the blank tile will be moved in.
     */
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

    /**
     * Helping method, will look for the given node in the given node. If that node
     * in the list has a higher depth, it will remove the on from the list with the
     * given node that has a lower depth/cost.
     * @param child the given node to check for
     * @param list the list of nodes that may be updated with the given node
     */
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

    /**
     * Helping method, will check if the called movement is a valid one.
     * Valid means:
     * -the blank tile is only allowed to move e.g. up, if there is
     * another tile to switch with, means the blank tile is not at the upper edge of
     * the puzzle field.
     * -the blank tile was not one move before up. So that it prevent the tile to
     * move down and up, because this will not lead to any change and is only a
     * unnecessary backward movement.
     * @param direction the blank tile should move, up, down, left or right
     * @param current the current state/node that stores the blank tile position
     *                and all other tile positions
     * @return true if the movement is valid, otherwise false
     */
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

    /**
     * Creates a new node / state from the parent one. The new state will differ
     * in the postion of the tile, that is moved in the given direction and in
     * the cost(new depth, new heuristic cost).
     * @param direction the blank tile moves to
     * @param current the current node from where the movement comes
     *               (will be the new parent node for that child)
     * @return the new node, with new position of the blank tile, called childNode
     */
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
        Node node = new Node(matrix, direction);
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

    public String getMoveSeq() {
        return moveSeq;
    }

    public Node getParentNode() {
        return parentNode;
    }

}