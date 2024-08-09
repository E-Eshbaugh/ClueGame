/*BoardCell
 * 
 * @author Colin Myers
 * @author Ethan Eshbaugh
 * 
 * Board cell class to keep track of where each type of cell is on board
 * info in class -> [current occupation, a room class, positions, and list of adjacent cells]
 */

package clueGame;


import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

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
		
		
		
		/*==========================
		 * use switch to set and 
		 * return a cells proper color
		 ===========================*/
		private Color getColor() {
			switch (Character.toUpperCase(roomSymbol.charAt(0))) {
				case 'N' :
					return Color.black;
					
				case 'W' :
					return Color.decode("#FFFF8F");
					
				case 'X' :
					return Color.darkGray;
					
				default :
					return Color.lightGray;
			}
		}
		
		
		
		/*=======================
		 * Gets the player on an 
		 * occupied cell to draw
		 ========================*/
		public Player playerToDraw(ArrayList<Player> playerList) {
			if (isOccupied) {
				for (Player player : playerList) {
					if (player.getRow() == row && player.getCol() == col) return player;
				}
			}
			return null;
		}
		
		
		
		/*=======================
		 * paint function so each 
		 * cell can paint itself
		 ========================*/
		public JPanel draw() {
		    JPanel cell = new JPanel() {
		        @Override
		        protected void paintComponent(Graphics g) {
		            super.paintComponent(g);
		            setBackground(getColor());
		            if (isDoorway()) {
		                drawDoorwayBorder(this);
		            } else if (getInitial().charAt(0) == 'W' && !isDoorway()) {
		                setBorder(new LineBorder(Color.black));
		            }
		        }
		    };
		    return cell;
		}

		private void drawDoorwayBorder(JPanel cell) {
		    Border border = null;
		    int thickBorder = 3;
		    int thinBorder = 0;

		    switch (this.getDoorDirection()) {
		        case UP:
		            border = BorderFactory.createMatteBorder(thickBorder, thinBorder, thinBorder, thinBorder, Color.BLUE);
		            break;
		        case DOWN:
		            border = BorderFactory.createMatteBorder(thinBorder, thinBorder, thickBorder, thinBorder, Color.BLUE);
		            break;
		        case LEFT:
		            border = BorderFactory.createMatteBorder(thinBorder, thickBorder, thinBorder, thinBorder, Color.BLUE);
		            break;
		        case RIGHT:
		            border = BorderFactory.createMatteBorder(thinBorder, thinBorder, thinBorder, thickBorder, Color.BLUE);
		            break;
		    }

		    if (border != null) {
		        cell.setBorder(BorderFactory.createCompoundBorder(border, new LineBorder(Color.black)));
		    }
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
	        return "(" + row + "," + col + ")";
	    }
	}