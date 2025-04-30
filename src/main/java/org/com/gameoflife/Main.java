package org.com.gameoflife;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.*;

public class Main {

    // Represents a cell's position (x, y)
    static class Coordinate {
        final int x, y;

        Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }

        // Required for HashSet to work correctly
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Coordinate other = (Coordinate) obj;
            return x == other.x && y == other.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    public static void main(String[] args) {

        File file = selectLifeFile();
        if (file == null) {
            System.out.println("Operation canceled.");
            return;
        }

        Set<Coordinate> liveCells = readLifeFile(file);
        if (liveCells == null) {
            System.out.println("Error: Invalid file.");
            return;
        }

//        System.out.println("Starting with " + liveCells.size() + " live cells.");
//
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("Enter the number of generations to simulate: ");
//        int generations = scanner.nextInt();

        for (int gen = 1; gen <= 10; gen++) {
            liveCells = simulateGeneration(liveCells);
            //System.out.println("Generation " + gen + ": " + liveCells.size() + " cells.");
        }

        printResults(liveCells);
    }

    private static File selectLifeFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select a Life file (.life or .lif)");
        fileChooser.setFileFilter(new FileNameExtensionFilter(
                "Life Files", "life", "lif"));

        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }

    static Set<Coordinate> readLifeFile(File file) {
        Set<Coordinate> liveCells = new HashSet<>();

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();

                // Skip empty lines and comments
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                // Split into x y coordinates
                String[] parts = line.split("\\s+");
                if (parts.length != 2) {
                    System.out.println("Warning: Skipping invalid line: " + line);
                    continue;
                }

                try {
                    int x = Integer.parseInt(parts[0]);
                    int y = Integer.parseInt(parts[1]);
                    liveCells.add(new Coordinate(x, y));
                } catch (NumberFormatException e) {
                    System.out.println("Warning: Skipping line with non-numbers: " + line);
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
            return null;
        }

        return liveCells;
    }

    static Set<Coordinate> simulateGeneration(Set<Coordinate> currentLiveCells) {
        Map<Coordinate, Integer> neighborCounts = new HashMap<>();

        for (Coordinate cell : currentLiveCells) {
            // Check all 8 possible neighbors
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    // Skip the cell itself
                    if (dx == 0 && dy == 0) continue;

                    Coordinate neighbor = new Coordinate(cell.x + dx, cell.y + dy);
                    // Increment the neighbor count for this cell
                    neighborCounts.put(neighbor,
                            neighborCounts.getOrDefault(neighbor, 0) + 1);
                }
            }
        }

        // Now determine which cells will be alive in the next generation
        Set<Coordinate> nextGeneration = new HashSet<>();
        for (Map.Entry<Coordinate, Integer> entry : neighborCounts.entrySet()) {
            Coordinate cell = entry.getKey();
            int neighbors = entry.getValue();
            boolean isCurrentlyAlive = currentLiveCells.contains(cell);

            // Game of Life rules:
            // 1. Any live cell with 2 or 3 neighbors survives
            // 2. Any dead cell with exactly 3 neighbors becomes alive
            if ((isCurrentlyAlive && (neighbors == 2 || neighbors == 3)) ||
                    (!isCurrentlyAlive && neighbors == 3)) {
                nextGeneration.add(cell);
            }
        }

        return nextGeneration;
    }

    // Prints results in Life 1.06 format
    private static void printResults(Set<Coordinate> liveCells) {
        System.out.println("# Life 1.06");

        // Sort output for readability
        List<Coordinate> sortedCells = new ArrayList<>(liveCells);
        sortedCells.sort((a, b) -> {
            if (a.x != b.x) return a.x - b.x;
            return a.y - b.y;
        });

        for (Coordinate cell : sortedCells) {
            System.out.println(cell.x + " " + cell.y);
        }
    }
}