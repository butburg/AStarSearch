package main.java;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {

    private static final String FILEPATH = "src/main/resources/";
    private static int numberN;
    private static int[][] goal = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 0}};
    private static int[][] goal4 = {
            {1, 2, 3, 4,},
            {5, 6, 7, 8},
            {9, 10, 11, 12},
            {13, 14, 15, 0},};

    public static void main(String[] args) throws FileNotFoundException {
        if (args.length != 2) {
            System.out.println("Wrong number of command line arguments. 2 arguments needed. \nExample: >java main.java.Main inFile.txt outFile.txt");
            System.exit(1);
        }

        int[][] inputMatrix = readFile(args[0]);
        if (inputMatrix.length == 4) goal = goal4;

        Puzzle puzzle = new Puzzle(inputMatrix, goal);
        Node startNode = puzzle.getParentNode();

        //here happens the magic
        Node finalNode = puzzle.calculate();

        StringBuilder output = new StringBuilder(startNode.printMatrix());

        if (finalNode == null) {
            output.append("After " + puzzle.getLIMIT() + " seen Nodes there was no Solution found. END");
            System.out.println(output);
            writeFile(args[1], output.toString());
            System.exit(1);
        }

        //weight and number of moves is same as any move has the cost of one
        double weight = finalNode.getDepth();
        String moves = puzzle.getMoves();
        int seenNodes = puzzle.getSeenNodes();

        output.append("Solution: ");
        output.append((int) weight);
        output.append(", ");
        output.append(moves);
        output.append("\n");
        output.append("States seen: ");
        output.append(seenNodes);

        System.out.println(output);

        writeFile(args[1], output.toString());
    }


    public static int[][] readFile(String fileInput) throws FileNotFoundException, NumberFormatException, IndexOutOfBoundsException {
        //build String Array from File
        // read file
        try (Scanner in = new Scanner(new File(FILEPATH + fileInput))) {

            numberN = Integer.parseInt(in.nextLine());
            int[][] matrix = new int[numberN][numberN];

            int i = 0;
            int j = 0;
            int lineNumber = numberN;

            while (in.hasNextLine() && lineNumber > 0) {
                // read numbers per line
                String row = in.nextLine();
                Scanner scRow = new Scanner(row);
                while (scRow.hasNextInt()) {
                    int digit = scRow.nextInt();
                    matrix[j][i] = digit;
                    i++;
                }
                i = 0;
                scRow.close();
                j++;
                lineNumber--;
            }
            in.close();

            if ((numberN != matrix.length) || (numberN != matrix[0].length)) {
                System.out.println("Number N ist not equal to the Dimension of the given Matrix!");
                System.exit(1);
            }

            return matrix;
        }
    }

    public static void writeFile(String fileOutput, String output) throws FileNotFoundException {
        //output the results into a file:
        try (PrintWriter out = new PrintWriter(FILEPATH + fileOutput)) {
            out.print(output);
        }
    }

}
