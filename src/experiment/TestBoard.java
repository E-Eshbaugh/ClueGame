package experiment;

import java.util.HashSet;
import java.util.Set;

public class TestBoard {
	private TestBoardCell[][] grid;
    private Set<TestBoardCell> targets;
    private static final int ROWS = 4;
    private static final int COLS = 4;

    public TestBoard() {
        grid = new TestBoardCell[ROWS][COLS];
        targets = new HashSet<>();
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                grid[row][col] = new TestBoardCell(row, col);
            }
        }
        setAdjacencies();
    }


    private void setAdjacencies() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                TestBoardCell cell = grid[row][col];
                if (row > 0) cell.addAdjacency(grid[row - 1][col]);
                if (row < ROWS - 1) cell.addAdjacency(grid[row + 1][col]);
                if (col > 0) cell.addAdjacency(grid[row][col - 1]);
                if (col < COLS - 1) cell.addAdjacency(grid[row][col + 1]);
            }
        }
    }

    public void calcTargets(TestBoardCell startCell, int pathLength) {
        targets.clear();
        Set<TestBoardCell> visited = new HashSet<>();
        visited.add(startCell);
        findAllTargets(startCell, pathLength, visited);
    }
    
    private void findAllTargets(TestBoardCell cell, int steps, Set<TestBoardCell> visited) {
        for (TestBoardCell adj : cell.getAdjList()) {
            if (visited.contains(adj) || adj.getOccupied()) continue;
            visited.add(adj);
            if (steps == 1 || adj.isRoom()) {
                targets.add(adj);
            } else {
                findAllTargets(adj, steps - 1, visited);
            }
            visited.remove(adj);
        }
    }


    public TestBoardCell getCell(int row, int col) {
        return grid[row][col];
    }

    public Set<TestBoardCell> getTargets() {
        return targets;
    }
}