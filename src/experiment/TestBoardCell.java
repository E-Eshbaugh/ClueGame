package experiment;

import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/*
 * Class TestBoardCell: One cell of the TestBoardClass contains information deciding if cell is
 * a room or occupied by player. 
 * 
 * By Ethan Eshbaugh and Colin Myers
 */
public class TestBoardCell {
    public int row;
    public int col;
    private boolean isRoom;
    private boolean isOccupied;
    private Set<TestBoardCell> adjList;

    public TestBoardCell(int row, int col) {
        this.row = row;
        this.col = col;
        this.isRoom = false;
        this.isOccupied = false;
        this.adjList = new HashSet<>();
    }

    public void addAdjacency(TestBoardCell cell) {
        adjList.add(cell);
    }

    public Set<TestBoardCell> getAdjList() {
        return adjList;
    }

    public void setRoom(boolean isRoom) {
        this.isRoom = isRoom;
    }

    public boolean isRoom() {
        return isRoom;
    }

    public void setOccupied(boolean isOccupied) {
        this.isOccupied = isOccupied;
    }

    public boolean getOccupied() {
        return isOccupied;
    }
    
    /*
     * Method equals: overided default equals to test for equal values rather than same memory spot
     * 
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestBoardCell that = (TestBoardCell) o;
        return row == that.row && col == that.col;
    } 
    
    /*
     * Method hashCode: equal objects must have same hash code this fixes default hash issue
     * 
     */
    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
    /*
     * Method toString: easier more readable print format
     * 
     */
    @Override
    public String toString() {
        return "Cell (" + row + "," + col + ")";
    }
}