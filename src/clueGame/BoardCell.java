/*BoardCell
 * 
 * @author Colin Myers
 * @author Ethan Eshbaugh
 * 
 * Board cell class to keep track of where each type of cell is on board
 */

package clueGame;


import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

//import clueGame.Board;

public class BoardCell {
    public int row;
    public int col;
    private String initial;
    private DoorDirection doorDirection;
    private boolean roomLabel;
    private boolean roomCenter;
    private char secretPassage;
    private Set<BoardCell> adjList;

    public BoardCell(int row, int col, String init) {
        this.row = row;
        this.col = col;
        this.adjList = new HashSet<>();
        initial = init;
    }

    // Getters and setters for the attributes
    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }
    
    public boolean isDoorway() {
        return doorDirection != DoorDirection.NONE;
    }
    
    public DoorDirection getDoorDirection() {
        return doorDirection;
    }

    public void setDoorDirection(DoorDirection doorDirection) {
        this.doorDirection = doorDirection;
    }

    public boolean isLabel() {
        return roomLabel;
    }

    public void setLabel(boolean roomLabel) {
        this.roomLabel = roomLabel;
    }

    public boolean isRoomCenter() {
        return roomCenter;
    }

    public void setRoomCenter(boolean roomCenter) {
        this.roomCenter = roomCenter;
    }

    public char getSecretPassage() {
        return secretPassage;
    }

    public void setSecretPassage(char secretPassage) {
        this.secretPassage = secretPassage;
    }

    public Set<BoardCell> getAdjList() {
        return adjList;
    }

    public void addAdj(BoardCell adj) {
        adjList.add(adj);
    }
    
    /*
     * Method equals: overided default equals to test for equal values rather than same memory spot
     * 
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board that = (Board) o;
        return row == that.numRows && col == that.numCols;
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