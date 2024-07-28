/*BoardCell
 * 
 * @author Colin Myers
 * @author Ethan Eshbaugh
 * 
 * Board cell class to keep track of where each type of cell is on board
 * info in class -> [current occupation, a room class, positions, and list of adjacent cells]
 */

package clueGame;


import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class BoardCell {
	 public int row;
	 public int col;
	 private String roomSymbol; //Rooms -> A,B,BS,C,D,DT,G,K,L,SB,T,TD 
	 							//(* and # variant for every single letter e.g. for A, there is also A* and A# too)
	 							//Non-Rooms -> N,W,W^,Wv,W>,W<,X
	 private Room room; // Reference to the Room class
	 private Set<BoardCell> adjList;
	 public boolean isOccupied;

	 
	 /*=====================
	  * BoardCell constructor
	  =======================*/
	 public BoardCell(int row, int col, String symbol) {
	        this.row = row;
	        this.col = col;
	        this.roomSymbol = symbol;
	        this.adjList = new HashSet<>();
	        isOccupied = false;
	    }

	 
	    /*======================================
	     * Getters and setters for the attributes
	     =======================================*/
	 	//[Rows]
	    public int getRow() {
	        return row;
	    }

	    public void setRow(int row) {
	        this.row = row;
	    }
	    
	    //[Columns]
	    public int getCol() {
	        return col;
	    }

	    public void setCol(int col) {
	        this.col = col;
	    }

	    //[Initial] 
	    public String getInitial() {
	        return roomSymbol;
	    }

	    public void setInitial(String symbol) {
	        this.roomSymbol = symbol;
	    }
	    
	    //[Rooms]
	    public Room getRoom() {
	        return room;
	    }

	    public void setRoom(Room room) {
	        this.room = room;
	    }
	    
	    //[Doorways]
	    public boolean isDoorway() {
	        return (room != null && room.getDoorDirection() != DoorDirection.NONE);
	    }

	    public DoorDirection getDoorDirection() {
	        return room != null ? room.getDoorDirection() : DoorDirection.NONE;
	    }

	    public void setDoorDirection(DoorDirection doorDirection) {
	        if (room != null) {
	            room.setDoorDirection(doorDirection);
	        }
	    }
	    
	    //[Label]
	    public boolean isLabel() {
	        return room != null && room.isLabel();
	    }

	    public void setLabel(boolean roomLabel) {
	            room.setLabel(roomLabel);
	    }
	    
	    //[Room Center]
	    public boolean isRoomCenter() {
	        return room != null && room.isRoomCenter();
	    }

	    public void setRoomCenter(boolean roomCenter) {
	        if (room != null) {
	            room.setRoomCenter(roomCenter);
	        }
	    }
	    
	    //[Secret Passage]
	    public char getSecretPassage() {
	        return room != null ? room.getSecretPassage() : '\0';
	    }

	    public void setSecretPassage(char secretPassage) {
	        if (room != null) {
	            room.setSecretPassage(secretPassage);
	        }
	    }
	    
	    //[Adjacency]
	    public Set<BoardCell> getAdjList() {
	        return adjList;
	    }

	    public void addAdj(BoardCell adj) {
	        adjList.add(adj);
	    }
	    
	    //[Occupation]
	    public void setOccupied(boolean b) {
			isOccupied = b;
			
		}
	
		public boolean getOccupied() {
			return isOccupied;
		}
	    
	    
	    /*=============================================
	     * equality override for boardCell comparison
	     ==============================================*/
	    @Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (o == null || getClass() != o.getClass()) return false;
	        BoardCell that = (BoardCell) o;
	        return row == that.row && col == that.col;
	    }
	    
	    
	    /*==================
	     * hash functions
	     ====================*/
	    @Override
	    public int hashCode() {
	        return Objects.hash(row, col);
	    }

	    
	    /*=================
	     * toString
	     ==================*/
	    @Override
	    public String toString() {
	        return "Cell (" + row + "," + col + ")";
	    }
	}