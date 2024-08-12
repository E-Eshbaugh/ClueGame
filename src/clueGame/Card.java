package clueGame;

import java.awt.Color;

public class Card {
    private String name;
    private CardType type;
    private boolean isAnswer;
    private boolean hasBeenDealt;
    private Color color;

    // Enum for card types
    public enum CardType {
        ROOM, PERSON, WEAPON
    }

    // Constructor
    public Card(String name, CardType type) {
        this.name = name;
        this.type = type;
        this.hasBeenDealt = false;
        this.isAnswer = false;
        this.color = null;
    }
    
    //constructor for gui testing with colors
    public Card(String name, CardType type, Color color) {
        this.name = name;
        this.type = type;
        this.hasBeenDealt = false;
        this.isAnswer = false;
        this.color = color;
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Getter for type
    public CardType getType() {
        return type;
    }
    
    //color setter
    public void setColor(Color color) {
    	this.color = color;
    }
    
    //color getter
    public Color getColor() {
    	return color;
    }

    // Override equals method
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Card card = (Card) obj;

        if (!name.equals(card.name)) return false;
        return type == card.type;
    }

    @Override
    public String toString() {
        return name;
    }

	public boolean getAnswer() {
		return isAnswer;
	}

	public void isAnswer(boolean isAnswer) {
		this.isAnswer = isAnswer;
	}

	public boolean isHasBeenDealt() {
		return hasBeenDealt;
	}

	public void setHasBeenDealt(boolean hasBeenDealt) {
		this.hasBeenDealt = hasBeenDealt;
	}
}
