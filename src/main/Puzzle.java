package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author github.com/butburg (EW) on Okt 2021
 */
public class Puzzle {

    private final int numberN;
    State state = new State();
    private int[] inputMatrix;
    private List<Integer> solutionSeq = new ArrayList<>();
    private List<Integer> priorityQueue = new ArrayList<>();
    private Map<Integer, State> visitedNodes = new HashMap<>();
    private int seenStates = 0;


    public Puzzle(int[] inputMatrix) {
        this.numberN = inputMatrix[0];
        this.inputMatrix = inputMatrix;
    }

    public int[] calculate() {

        return new int[0];

    }
}
