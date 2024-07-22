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
	 private Room room; // Reference to the Room class
	 private Set<BoardCell> adjList;

	 public BoardCell(int row, int col, String initial) {
	        this.row = row;
	        this.col = col;
	        this.initial = initial;
	        this.adjList = new HashSet<>();
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

	    public Room getRoom() {
	        return room;
	    }

	    public void setRoom(Room room) {
	        this.room = room;
	    }

	    public boolean isDoorway() {
	        return room != null && room.getDoorDirection() != DoorDirection.NONE;
	    }

	    public DoorDirection getDoorDirection() {
	        return room != null ? room.getDoorDirection() : DoorDirection.NONE;
	    }

	    public void setDoorDirection(DoorDirection doorDirection) {
	        if (room != null) {
	            room.setDoorDirection(doorDirection);
	        }
	    }

	    public boolean isLabel() {
	        return room != null && room.isLabel();
	    }

	    public void setLabel(boolean roomLabel) {
	            room.setLabel(roomLabel);
	    }

	    public boolean isRoomCenter() {
	        return room != null && room.isRoomCenter();
	    }

	    public void setRoomCenter(boolean roomCenter) {
	        if (room != null) {
	            room.setRoomCenter(roomCenter);
	        }
	    }

	    public char getSecretPassage() {
	        return room != null ? room.getSecretPassage() : '\0';
	    }

	    public void setSecretPassage(char secretPassage) {
	        if (room != null) {
	            room.setSecretPassage(secretPassage);
	        }
	    }

	    public Set<BoardCell> getAdjList() {
	        return adjList;
	    }

	    public void addAdj(BoardCell adj) {
	        adjList.add(adj);
	    }

	    @Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (o == null || getClass() != o.getClass()) return false;
	        BoardCell that = (BoardCell) o;
	        return row == that.row && col == that.col;
	    }

	    @Override
	    public int hashCode() {
	        return Objects.hash(row, col);
	    }

	    @Override
	    public String toString() {
	        return "Cell (" + row + "," + col + ")";
	    }
	}