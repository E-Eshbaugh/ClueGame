package tests;

//
//
//import java.util.HashSet;
//import java.util.Set;
//
//public class TestBoard {
//    private TestBoardCell[][] grid;
//    private Set<TestBoardCell> targets;
//
//    public TestBoard() {
//        grid = new TestBoardCell[4][4];
//        targets = new HashSet<>();
//        for (int row = 0; row < 4; row++) {
//            for (int col = 0; col < 4; col++) {
//                grid[row][col] = new TestBoardCell(row, col);
//            }
//        }
//        setAdjacencies();
//    }
//
//    private void setAdjacencies() {
//        for (int row = 0; row < 4; row++) {
//            for (int col = 0; col < 4; col++) {
//                TestBoardCell cell = grid[row][col];
//                if (row > 0) cell.addAdjacency(grid[row - 1][col]);
//                if (row < 3) cell.addAdjacency(grid[row + 1][col]);
//                if (col > 0) cell.addAdjacency(grid[row][col - 1]);
//                if (col < 3) cell.addAdjacency(grid[row][col + 1]);
//            }
//        }
//    }
//
//    public void calcTargets(TestBoardCell startCell, int pathLength) {
//        targets.clear();
//        Set<TestBoardCell> visited = new HashSet<>();
//        visited.add(startCell);
//        findAllTargets(startCell, pathLength, visited);
//    }
//
//    private void findAllTargets(TestBoardCell cell, int steps, Set<TestBoardCell> visited) {
//        for (TestBoardCell adj : cell.getAdjList()) {
//            if (visited.contains(adj) || adj.getOccupied()) continue;
//            visited.add(adj);
//            if (steps == 1 || adj.isRoom()) {
//                targets.add(adj);
//            } else {
//                findAllTargets(adj, steps - 1, visited);
//            }
//            visited.remove(adj);
//        }
//    }
//
//    public TestBoardCell getCell(int row, int col) {
//        return grid[row][col];
//    }
//
//    public Set<TestBoardCell> getTargets() {
//        return targets;
//    }
//}


public class Filler {

}

