package clueGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AccusationDialog extends JDialog {
    private JComboBox<String> personComboBox;
    private JComboBox<String> weaponComboBox;
    private JComboBox<String> roomComboBox;
    private JButton submitButton;
    private JButton cancelButton;
    private Solution accusation;

    public AccusationDialog(JFrame parent, ArrayList<String> people, ArrayList<String> weapons, ArrayList<String> rooms) {
        super(parent, "Make an Accusation", true);
        setLayout(new GridLayout(5, 2));
        setSize(400, 200);
        setLocationRelativeTo(parent);

        // Room selection
        add(new JLabel("Room:"));
        roomComboBox = new JComboBox<>(rooms.toArray(new String[0]));
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

                // Create the accusation
                accusation = new Solution(new Card(selectedRoom, Card.CardType.ROOM),
                                          new Card(selectedPerson, Card.CardType.PERSON),
                                          new Card(selectedWeapon, Card.CardType.WEAPON));
                
                // Close the dialog
                setVisible(false);
                dispose();
            }
        });
        add(submitButton);

        // Cancel button
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accusation = null;
                setVisible(false);
                dispose();
            }
        });
        add(cancelButton);
    }

    public Solution getAccusation() {
        return accusation;
    }
}