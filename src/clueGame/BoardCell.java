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
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

//auto generated suppress warning
@SuppressWarnings("serial")
public class BoardCell extends JPanel{
	 public int row;
	 public int col;
	 private String roomSymbol; //Rooms -> A,B,BS,C,D,DT,G,K,L,SB,T,TD 
	 							//(* and # variant for every single letter e.g. for A, there is also A* and A# too)
	 							//Non-Rooms -> N,W,W^,Wv,W>,W<,X
	 private Room room; // Reference to the Room class
	 private Set<BoardCell> adjList;
	 public boolean isOccupied;
	 private boolean isHighlighted;
	 private ArrayList<Player> players; // Add this member variable
	 public int numPlayersInRoom;
	 private int numInRoom;
	 

	 
	 /*=====================
	  * BoardCell constructor
	  =======================*/
	 public BoardCell(int row, int col, String symbol) {
	        this.row = row;
	        this.col = col;
	        this.roomSymbol = symbol;
	        this.adjList = new HashSet<>();
	        isOccupied = false;
	        this.setOpaque(true);
	        this.isHighlighted = false;
	        this.numPlayersInRoom = 0;
	        this.numInRoom = 1;
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
	    
	
		public boolean getOccupied() {
			return isOccupied;
		}
		
		public void setPlayers(ArrayList<Player> players) {
	        this.players = players;
	        repaint(); // Repaint when the players list is set
	    }

	    // Method to set highlight
	    public void setHighlighted(boolean highlighted) {
	        this.isHighlighted = highlighted;
	        repaint();
	    }

	    // Method to set occupation
	    public void setOccupied(boolean occupied) {
	    	if(occupied) {
	    		numPlayersInRoom++;
	    	}
	    	if(!occupied) {
	    		numPlayersInRoom--;
	    	}
	    	if(occupied && this.isOccupied) {
	    		numInRoom = numPlayersInRoom;
	    	}
	        this.isOccupied = occupied;
	        repaint();
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
		public JPanel draw(ArrayList<Player> players) {
		    this.setPlayers(players); 
		    return this;
		}
		
		
		@Override
		protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        setBackground(getColor());

	        if (isDoorway()) {
	            drawDoorwayBorder(this);
	        } else if (getInitial().charAt(0) == 'W' && !isDoorway()) {
	            setBorder(new LineBorder(Color.black));
	        }

	        // Draw the highlight if needed
	        if (isHighlighted) {
	            g.setColor(new Color(0, 0, 255, 100)); // Semi-transparent blue for highlight
	            g.fillRect(0, 0, getWidth(), getHeight());
	        }

	        // Draw the player piece if the cell is occupied
	        if (isOccupied && numPlayersInRoom >1) {
	        	Player player = playerToDraw(players);
	            if (player != null) {
	                g.setColor(player.getColor());
	                int padding = 2;
	                int diameter = Math.min(getWidth(), getHeight()) - 2 * padding;
	                g.fillOval(padding+(3+(numInRoom-1)), padding, diameter, diameter);
//	                System.out.println(numPlayersInRoom);
	            }
	        } else if(isOccupied){
	        	Player player = playerToDraw(players);
	            if (player != null) {
	                g.setColor(player.getColor());
	                int padding = 2;
	                int diameter = Math.min(getWidth(), getHeight()) - 2 * padding;
	                g.fillOval(padding, padding, diameter, diameter);
	            }
	        }
	        
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
		        default:
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