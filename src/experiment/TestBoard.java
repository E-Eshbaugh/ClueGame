/*TestBoard class
 * 
 * @author Colin Myers
 * @author Ethan Eshbaugh
 * 
 * Creates a sample board with dimensions 4x4
 * 
 */

package experiment;

import java.util.HashSet;
import java.util.Set;

public class TestBoard {
	private TestBoardCell[][] board;
    private Set<TestBoardCell> targets;
    private static final int ROWS = 4;
    private static final int COLS = 4;

    //initialize and set up board
    public TestBoard() {
        board = new TestBoardCell[ROWS][COLS];
        targets = new HashSet<>();
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                board[row][col] = new TestBoardCell(row, col);
            }
        }
        setAdjacencies();
    }

    //initializes cell adjacency lists for board, only runs once for whole game
    private void setAdjacencies() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                TestBoardCell cell = board[row][col];
                if (row > 0) cell.addAdjacency(board[row - 1][col]);
                if (row < ROWS - 1) cell.addAdjacency(board[row + 1][col]);
                if (col > 0) cell.addAdjacency(board[row][col - 1]);
                if (col < COLS - 1) cell.addAdjacency(board[row][col + 1]);
            }
        }
    }
    
    //BFD search, calculating targets for where player can move
    public void calcTargets(TestBoardCell startCell, int pathLength) {
        targets.clear();
        Set<TestBoardCell> visited = new HashSet<>();
        visited.add(startCell);
        findAllTargets(startCell, pathLength, visited);
    }
    
    //part of BFD search for finding movement targets 3 steps away
    private void findAllTargets(TestBoardCell cell, int steps, Set<TestBoardCell> visited) {
    	
    	//for all adjacent spots to each cell, starting at desired first cell
        for (TestBoardCell adj : cell.getAdjList()) {
        	//if cell is occupied skip this iteration
            if (visited.contains(adj) || adj.getOccupied()) continue;
            //add cell's adjacencies to visited
            visited.add(adj);
            //if at last step, add to targets list
            if (steps == 1 || adj.isRoom()) {
                targets.add(adj);
            //continue until at step 3
            } else {
                findAllTargets(adj, steps - 1, visited);
            }
            visited.remove(adj);
        }
    }

    //return desired cell from board
    public TestBoardCell getCell(int row, int col) {
        return board[row][col];
    }
    
    //return all movement targets
    public Set<TestBoardCell> getTargets() {
        return targets;
    }
}