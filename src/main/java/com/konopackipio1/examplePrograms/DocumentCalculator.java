package com.konopackipio1.examplePrograms;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

// Program calculates values based on a given document
// Document should be formatted like this:
// 1 + 2
// 3 * 3
// etc....

//TODO: Dealing with eventual exceptions

public class DocumentCalculator {

    public static void calculateFile(String pathString) {
        Path path = Paths.get(pathString);

        try (BufferedReader reader = Files.newBufferedReader(path);
            BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.WRITE)) {

                String line;
                while((line = reader.readLine()) != null) {
                    String newLine = calculateLine(line);
                    writer.write(newLine);
                    writer.write("\n");
                }           

        } catch (IOException e) {
            System.err.println("Could not open the file: " + pathString);
        }

        
    }

    private static String calculateLine(String line) {
        String result = line;
        int x1, x2;
        char sign;

        String[] split = result.split("\\s");
        x1 = Integer.parseInt(split[0]);
        x2 = Integer.parseInt(split[2]);
        sign = split[1].charAt(0);     

        return result + " = " + calculateValue(x1, x2, sign);
    }

    private static int calculateValue(int x1, int x2, char sign) {
        switch (sign) {
            case '+':
                return x1 + x2;        
            case '-':                
                return x1 - x2;
            case '*':
                return x1 * x2;
            case '/':
                return x1/x2;        
            default:
                return 0;
        }
    }

    
}

class DocumentCalculatorRun {
    public static void main(String[] args) {
        DocumentCalculator.calculateFile("files/testFile.txt");
    }
}
