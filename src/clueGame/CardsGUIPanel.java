package clueGame;
/* CardsGUIPanel
 * 
 * @author Ethan Eshbaugh
 * @author Colin Meyers
 * 
 * Creates the GUI for the players seen cards on the right side of board
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

//auto added suggestion to suppress warning
@SuppressWarnings("serial")
public class CardsGUIPanel extends JPanel {
	
	private ArrayList<Card> playerHand = new ArrayList<Card> ();
	private ArrayList<Card> playerSeen = new ArrayList<Card> ();
	
	//Use later when game is more refined
	//private static ArrayList<Card> playerHand = Board.getHuman().getHand();
	//private ArrayList<Card> playerSeen = Board.getHuman().getSeen();
	
	//Use for now to initiate lists to make sure methods work
	private void cardsInitiate() {
		//hand
		playerHand.add(new Card("Room1", Card.CardType.ROOM, Color.YELLOW));
		playerHand.add(new Card("weapon1", Card.CardType.WEAPON, Color.RED));
		playerHand.add(new Card("person1", Card.CardType.PERSON, Color.black));
		
		//seen
		playerSeen.add(new Card("Room2", Card.CardType.ROOM, Color.blue));
		playerSeen.add(new Card("weapon2", Card.CardType.WEAPON, Color.GREEN));
		playerSeen.add(new Card("person2", Card.CardType.PERSON));
	}
	
	/*===================================
	 * Creates the GUI panel for cards
	 * 
	 * uses createCardsPanel()
	 ====================================*/
	public CardsGUIPanel() {
		// comment out later when using real player hands
		cardsInitiate();
		//=================================================
		
		setLayout(new GridLayout(1,0));
		JPanel panel = createCardsPanel();
		add(panel);
	}
	
	
	
	/*==================================
	 * Creates three panels, 1 for each 
	 * card type with in hand and seen
	 * cards of those types in them
	 * 
	 * uses createCatagory Frame
	 =================================*/
	private JPanel createCardsPanel() {
		JPanel panel = new JPanel();
		// Use a grid layout, 3 rows, 1 column
		panel.setLayout(new GridLayout(3,1));
		JPanel peopleFrame = createCatagoryFrame("People");
		JPanel roomFrame = createCatagoryFrame("Rooms");
		JPanel weaponFrame = createCatagoryFrame("Weapons");
		panel.add(peopleFrame);
		panel.add(roomFrame);
		panel.add(weaponFrame);
		
		panel.setBorder(new TitledBorder (new EtchedBorder(), "Known Cards"));
		return panel;
	}
	
	
	
	/*===================================
	 * Creates a category panel which
	 * has the seen and in hand cards
	 * of each category
	 * 
	 * uses cardToPanel()
	 ===================================*/
	private JPanel createCatagoryFrame(String type) {
		Card.CardType lookForType = null;
		ArrayList<Card> toShowHand = new ArrayList<Card>();
		ArrayList<Card> toShowSeen = new ArrayList<Card>();
		
		switch(type) {
		case "People":
			lookForType = Card.CardType.PERSON;
			break;
		case "Rooms":
			lookForType = Card.CardType.ROOM;
			break;
		case "Weapons":
			lookForType = Card.CardType.WEAPON;
			break;
		}
		
		for(Card card : playerHand) if (card.getType().equals(lookForType)) toShowHand.add(card);
		for(Card card : playerSeen) if (card.getType().equals(lookForType)) toShowSeen.add(card);
		
		if(toShowHand.isEmpty()) toShowHand.add(new Card("None", lookForType));
		if(toShowHand.isEmpty()) toShowSeen.add(new Card("None", lookForType));
		
		int numRows = toShowHand.size() + toShowSeen.size()+2;
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(numRows,1));
		
		JLabel inHand = new JLabel();
		inHand.setText("In Hand:");
		JLabel seen = new JLabel();
		seen.setText("Seen:");
		
		panel.add(inHand);
		for(Card card : toShowHand) panel.add(cardToTextField(card));
		panel.add(seen);
		for(Card card : toShowSeen) panel.add(cardToTextField(card));
		
		panel.setBorder(new TitledBorder (new EtchedBorder(), type));
		return panel;
	}
	
	
	
	/*==========================
	 * takes a card and turns it
	 * into a panel to be put into 
	 * the card panel GUI
	============================== */
	private JTextField cardToTextField(Card card) {
		JTextField field = new JTextField();
		field.setFont(field.getFont().deriveFont(Font.BOLD));
		field.setText(card.getName());
		field.setBackground(card.getColor());
		
		
		return field;
	}
	
	
	
	public static void main(String[] args) {
		// Create a JFrame with all the normal functionality
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("GUI Example");
		frame.setSize(250, 500);
		// Create the JPanel and add it to the JFrame
		CardsGUIPanel gui = new CardsGUIPanel();
		frame.add(gui, BorderLayout.CENTER);
		// Now let's view it
		frame.setVisible(true);
	}
}
