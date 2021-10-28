package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {

    private static final String FILEPATH = "src/main/resources/";


    public static void main(String[] args) throws FileNotFoundException {
        if (args.length != 2) {
            System.out.println("Wrong number of command line arguments. 2 arguments needed. \nExample: >java main.Main inFile.txt outFile.txt");
            System.exit(1);
        }

        int[][] goal = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};

        Puzzle puzzle = new Puzzle(readFile(args[0]), goal);

        int[][] resultMatrix = puzzle.calculate().getMatrixCopy();

        StringBuilder result = new StringBuilder();
        for (int[] row : resultMatrix) {
            for (int digit : row) {
                result.append(digit).append(" ");
            }
        }
        writeFile(args[1], result.toString());
    }


    public static int[][] readFile(String fileInput) throws FileNotFoundException, NumberFormatException, IndexOutOfBoundsException {
        //build String Array from File
        // read file
        try (Scanner in = new Scanner(new File(FILEPATH + fileInput))) {
            int numberN = Integer.parseInt(in.nextLine());
            int[][] matrix = new int[numberN][numberN];

            int i = 0;
            int j = 0;

            while (in.hasNextLine() && numberN > 0) {
                // read numbers per line
                String row = in.nextLine();
                Scanner scRow = new Scanner(row);
                while (scRow.hasNextInt()) {
                    int digit = scRow.nextInt();
                    matrix[j][i] = digit;
                    i++;
                }
                i=0;
                scRow.close();
                j++;
                numberN--;
            }
            in.close();
            return matrix;
        }

    }

    public static void writeFile(String fileOutput, String output) throws FileNotFoundException {
        //output the results into a file:
        try (PrintWriter out = new PrintWriter(FILEPATH + fileOutput)) {
            out.print(output);
        }//end try
    }


//
//        s.getTs().forEach(i -> {
//            System.out.print((i + " "));
//            out.print((i + " "));
//        });
//
//        System.out.println();
//
//
//        s.getSequence().forEach((key, value) -> {
//            System.out.print(value + "[" + key + "]" + " ");
//            out.print(value + "[" + key + "]" + " ");
//        });

}
