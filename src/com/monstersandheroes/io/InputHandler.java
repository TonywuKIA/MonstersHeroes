package com.monstersandheroes.io;

import java.util.Scanner;

/**
 * Thin wrapper over System.in for testability.
 */
public class InputHandler {
    private final Scanner scanner = new Scanner(System.in);

    public String readCommand() {
        return scanner.nextLine().trim();
    }

    public String readLine(String prompt) {
        if (prompt != null && !prompt.isEmpty()) {
            System.out.print(prompt);
        }
        return scanner.nextLine().trim();
    }

    public int readInt(String prompt, int min, int max) {
        while (true) {
            try {
                String line = readLine(prompt);
                int value = Integer.parseInt(line);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("Please enter a number between %d and %d%n", min, max);
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
