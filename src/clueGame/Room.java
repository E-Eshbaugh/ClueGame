package clueGame;
/* Room class
 * 
 * @author Colin Myers
 * @author Ethan Eshbaugh
 * 
 * Room class to keep track of room names and functionality
 */

public class Room {
    private String name;
    private boolean roomLabel;
    private boolean roomCenter;
    private char secretPassage;
    private DoorDirection doorDirection = DoorDirection.NONE; 
    private BoardCell labelCell;
    private BoardCell centerCell;

    // Getters and setters for the attributes
    public Room(String roomName) {
        this.name = roomName;
        roomLabel = false;
    }

    public String getName() {
        return name;
        
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isLabel() {
        return roomLabel;
    }

    public void setLabel(boolean roomLabel) {
        this.roomLabel = roomLabel;
    }

    public boolean isRoomCenter() {
        return roomCenter;
    }

    public void setRoomCenter(boolean roomCenter) {
        this.roomCenter = roomCenter;
    }

    public char getSecretPassage() {
        return secretPassage;
    }

    public void setSecretPassage(char secretPassage) {
        this.secretPassage = secretPassage;
    }

    public DoorDirection getDoorDirection() {
        return doorDirection;
    }

    public void setDoorDirection(DoorDirection doorDirection) {
        this.doorDirection = doorDirection;
    }

    public BoardCell getLabelCell() {
        return labelCell;
    }

    public void setLabelCell(BoardCell labelCell) {
        this.labelCell = labelCell;
    }

    public BoardCell getCenterCell() {
        return centerCell;
    }

    public void setCenterCell(BoardCell centerCell) {
        this.centerCell = centerCell;
    }
}