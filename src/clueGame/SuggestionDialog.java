package clueGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SuggestionDialog extends JDialog {
    private JComboBox<String> personComboBox;
    private JComboBox<String> weaponComboBox;
    private JComboBox<String> roomComboBox;
    private JButton submitButton;
    private Solution suggestion;

    public SuggestionDialog(JFrame parent, String room, ArrayList<String> people, ArrayList<String> weapons) {
        super(parent, "Make a Suggestion", true);
        setLayout(new GridLayout(4, 2));
        setSize(400, 200);
        setLocationRelativeTo(parent);

        // Room is pre-selected and not changeable
        add(new JLabel("Room:"));
        roomComboBox = new JComboBox<>(new String[]{room});
        roomComboBox.setEnabled(false);
        add(roomComboBox);

        // People selection
        add(new JLabel("Person:"));
        personComboBox = new JComboBox<>(people.toArray(new String[0]));
        add(personComboBox);

        // Weapons selection
        add(new JLabel("Weapon:"));
        weaponComboBox = new JComboBox<>(weapons.toArray(new String[0]));
        add(weaponComboBox);

        // Submit button
        submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedRoom = (String) roomComboBox.getSelectedItem();
                String selectedPerson = (String) personComboBox.getSelectedItem();
                String selectedWeapon = (String) weaponComboBox.getSelectedItem();

                // Create the suggestion
                suggestion = new Solution(new Card(selectedRoom, Card.CardType.ROOM),
                                          new Card(selectedPerson, Card.CardType.PERSON),
                                          new Card(selectedWeapon, Card.CardType.WEAPON));
                
                // Close the dialog
                setVisible(false);
                dispose();
            }
        });
        add(new JLabel());  // Placeholder
        add(submitButton);
    }

    public Solution getSuggestion() {
        return suggestion;
    }
}