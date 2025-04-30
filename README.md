# Conway's Game of Life - Java Implementation

## Technology Stack
- **Language**: Java 17
- **Dependencie Management**: Maven

## Development Approach
We adopted a **test-first methodology** similar to TDD:
1. Created Life 1.06 files (`.life`/`.lif`) containing:
    - Standard patterns (Glider, Blinker)
    - Edge cases (empty files, malformed inputs)
2. Continuously refined the implementation by:
    - Running local simulations
    - Identifying anomalies
    - Adjusting the logic iteratively

## Application Rules

### Core Architecture
- **Dynamic Grid Handling**:
    - Used `Map` and `Set` collections for coordinate management
    - Benefits:
        - Easy implementation
        - Automatic duplicate handling
        - Good performance characteristics

### File Processing
- **File Selection**:
    - Implemented with `JFileChooser` because:
        - Native Java library
        - Cross-platform compatibility
        - Built-in file filtering

- **Coordinate Storage**:
  ```java
  class Coordinate {
      final int x, y;
      // Proper equals() and hashCode() implementations
  }

- **File Reading**:
    - Used `Scanner` for reading `.life` files
    - Ignored comment lines (starting with `#`)
    - Validated coordinate formats (ensured no letters in coordinates)
    - Split coordinates using the `split` function

  
### Advance Generation:

- We will advance 10 generations based on the cells stored in the Set:
    - We created a HashMap to store the cell coordinates as the key and the number of neighbors as the value (both for the cell itself and its neighbors)
    - At this point, I couldn’t find an alternative other than using a nested loop to iterate through all neighboring cells of the given coordinate, ignoring only the cell itself.
    - After mapping, we create a new Set for the next generation.
    - Then, we have another loop to apply the Game of Life rules:
      - Any live cell with 2 or 3 neighbors survives
      - Any dead cell with exactly 3 neighbors becomes alive
    - We will insert into the next generation Set only the cells that were born and those that remained alive.

  Finally, we print the current coordinates of the living cells to validate whether the system behaved correctly.

## Things that I cant do it:

- I couldn't find a .life file to represent a pulsar and test it, but I've tested a Toad and a Beacon.