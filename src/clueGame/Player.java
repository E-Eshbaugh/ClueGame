package clueGame;

import java.awt.Color;
import java.util.ArrayList;

public abstract class Player {
    private String name;
    private Color color;
    private int row;
    private int column;
    private ArrayList<Card> hand;

    // Constructor
    public Player(String name, Color color, int row, int column) {
        this.name = name;
        this.color = color;
        this.row = row;
        this.column = column;
        this.hand = new ArrayList<>();
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Getter for color
    public Color getColor() {
        return color;
    }

    // Getter for row
    public int getRow() {
        return row;
    }

    // Getter for column
    public int getColumn() {
        return column;
    }

    // Method to update the hand with a new card
    public void updateHand(Card card) {
        hand.add(card);
    }

    // Getter for hand
    public ArrayList<Card> getHand() {
        return hand;
    }

    // Abstract method to be implemented by subclasses
    public abstract void makeMove();

    // Override toString method (optional, for better readability)
    @Override
    public String toString() {
        return "Player" + "name='" + name + '\'' + ", color=" + color + ", row=" + row + ", column=" + column + ", hand=" + hand;
    }
}
