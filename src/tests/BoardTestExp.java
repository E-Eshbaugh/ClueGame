package tests;

import experiment.TestBoard;
import experiment.TestBoardCell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BoardTestExp {
    private TestBoard board;

    @BeforeEach
    public void setUp() {
        board = new TestBoard();
    }

    @Test
    public void testAdjacencyTopLeftCorner() {
        TestBoardCell cell = board.getCell(0, 0);
        Set<TestBoardCell> adjList = cell.getAdjList();
        assertEquals(2, adjList.size());
        assertTrue(adjList.contains(board.getCell(0, 1)));
        assertTrue(adjList.contains(board.getCell(1, 0)));
    }

    @Test
    public void testAdjacencyBottomRightCorner() {
        TestBoardCell cell = board.getCell(3, 3);
        Set<TestBoardCell> adjList = cell.getAdjList();
        assertEquals(2, adjList.size());
        assertTrue(adjList.contains(board.getCell(2, 3)));
        assertTrue(adjList.contains(board.getCell(3, 2)));
    }

    @Test
    public void testAdjacencyRightEdge() {
        TestBoardCell cell = board.getCell(1, 3);
        Set<TestBoardCell> adjList = cell.getAdjList();
        assertEquals(3, adjList.size());
        assertTrue(adjList.contains(board.getCell(0, 3)));
        assertTrue(adjList.contains(board.getCell(1, 2)));
        assertTrue(adjList.contains(board.getCell(2, 3)));
    }

    @Test
    public void testAdjacencyLeftEdge() {
        TestBoardCell cell = board.getCell(3, 0);
        Set<TestBoardCell> adjList = cell.getAdjList();
        assertEquals(3, adjList.size());
        assertTrue(adjList.contains(board.getCell(2, 0)));
        assertTrue(adjList.contains(board.getCell(3, 1)));
        assertTrue(adjList.contains(board.getCell(3, 0)));
    }

    @Test
    public void testTargetsEmptyBoard() {
        TestBoardCell cell = board.getCell(0, 0);
        board.calcTargets(cell, 3);
        Set<TestBoardCell> targets = board.getTargets();
        assertEquals(6, targets.size());
        assertTrue(targets.contains(board.getCell(0, 3)));
        assertTrue(targets.contains(board.getCell(1, 2)));
        assertTrue(targets.contains(board.getCell(2, 1)));
        assertTrue(targets.contains(board.getCell(3, 0)));
        assertTrue(targets.contains(board.getCell(2, 3)));
        assertTrue(targets.contains(board.getCell(3, 2)));
    }

    @Test
    public void testTargetsWithOccupiedCells() {
        board.getCell(1, 0).setOccupied(true);
        board.getCell(2, 1).setOccupied(true);
        TestBoardCell cell = board.getCell(0, 0);
        board.calcTargets(cell, 3);
        Set<TestBoardCell> targets = board.getTargets();
        //System.out.println(targets);
        assertEquals(3, targets.size());
        assertTrue(targets.contains(board.getCell(0, 3)));
        assertTrue(targets.contains(board.getCell(1, 2)));
        assertTrue(targets.contains(board.getCell(3, 0)));
    }

    @Test
    public void testTargetsWithRoomCells() {
        board.getCell(2, 1).setRoom(true);
        TestBoardCell cell = board.getCell(0, 0);
        board.calcTargets(cell, 3);
        Set<TestBoardCell> targets = board.getTargets();
        System.out.println(targets);
        assertEquals(6, targets.size());
        assertTrue(targets.contains(board.getCell(0, 3)));
        assertTrue(targets.contains(board.getCell(1, 2)));
        assertTrue(targets.contains(board.getCell(2, 1)));  // Room cell
        assertTrue(targets.contains(board.getCell(3, 0)));
        assertTrue(targets.contains(board.getCell(2, 3)));
        assertTrue(targets.contains(board.getCell(3, 2)));
    }
}