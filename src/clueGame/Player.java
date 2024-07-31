
/*Player Abstract Class
 * 
 * @author Ethan Eshbaugh
 * @author Colin Meyers
 * 
 * Outlines the abstract behaviors and characteristics for a player object in Clue game
 * Parent to HumanPlayer and ComputerPlayer
 * 
 * 1 human player and 5 bot players
 * 
 * 7/31/2024
 * 
 */

package clueGame;

import java.awt.*;
import java.util.*;

public abstract class Player {
	
	private String name;
	private Color pieceColor;
	private int row, col;
	private ArrayList<Card> hand;
	private boolean isHuman;
	
	/*=======================
	 * Player Constructor
	 =========================*/
	public Player(String name, Color color, int row, int col, boolean human) {
		this.name = name;
		pieceColor = color;
		this.row = row;
		this.col = col;
		isHuman = human;
	}
	
	/*===================
	 * Add card to hand
	 ===================*/
	public void updateHand(Card card) {
		hand.add(card);
	}
	
	/*==========================
	 * Getters & setters
	 =========================*/
	public boolean isHuman() {
		return isHuman;
	}
	
	public String getName() {
		return name;
	}
	
	public Color getColor() {
		return pieceColor;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
	
	public void setRow(int row) {
		this.row = row;
	}
	
	public void setCol(int col) {
		this.col = col;
	}
	
	public ArrayList<Card> getHand() {
		return hand;
	}
    // Override toString method (optional, for better readability)
    @Override
    public String toString() {
        return "Player" + "name='" + name + '\'' + ", color=" + pieceColor + ", row=" + row + ", column=" + col + ", hand=" + hand;
    }
    
    public abstract void makeMove();
}
