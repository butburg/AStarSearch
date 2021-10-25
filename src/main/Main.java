package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        if (args.length != 2) {
            System.out.println("Wrong number of command line arguments. 2 arguments needed. \nExample: >java main.Main inFile.txt outFile.txt");
            System.exit(1);
        }

        Puzzle puzzle = new Puzzle(readFile(args[0]));
        int[] resultMatrix = puzzle.calculate();

        StringBuilder result = new StringBuilder();
        for (int digit : resultMatrix) {
            result.append(digit).append(" ");
        }
        writeFile(args[1], result.toString());
    }


    public static int[] readFile(String fileInput) throws FileNotFoundException, NumberFormatException, IndexOutOfBoundsException {
        //build String Array from File
        // read file
        try (Scanner in = new Scanner(new File(fileInput))) {
            int numberN = Integer.parseInt(in.nextLine());
            int[] matrix = new int[(numberN * numberN) + 1];
            matrix[0] = numberN;
            int i = 1;
            while (in.hasNextLine()) {
                // read numbers per line
                String row = in.nextLine();
                Scanner scRow = new Scanner(row);
                while (scRow.hasNextInt()) {
                    matrix[i] = scRow.nextInt();
                }
            }
            return matrix;
        }

    }

    public static void writeFile(String fileOutput, String output) throws FileNotFoundException {
        //output the results into a file:
        try (PrintWriter out = new PrintWriter(fileOutput)) {
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
