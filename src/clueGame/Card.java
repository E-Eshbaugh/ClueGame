package clueGame;

public class Card {
    private String name;
    private CardType type;
    private boolean isAnswer;
    private boolean hasBeenDealt;

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
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Getter for type
    public CardType getType() {
        return type;
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
        return "Card " + "name= " + name  + ", type= " + type;
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
