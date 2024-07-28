package clueGame;
/* Room class
 * 
 * @author Colin Myers
 * @author Ethan Eshbaugh
 * 
 * Room class to keep track of room names and functionality 
 * info in class -> [door status, center or label, and secret passage status]
 */

public class Room {
    private String roomName;
    private boolean isRoomLabel;
    private boolean isRoomCenter;
    private boolean isRoomSecret;
    private char secretPassage;
    private DoorDirection doorDirection = DoorDirection.NONE; 
    private BoardCell labelCell;
    private BoardCell centerCell;

    /*=======================================
     * Constructor
     =======================================*/
    public Room(String roomName) {
        this.roomName = roomName;
        isRoomLabel = false;
        isRoomSecret = false;
    }

    
    /*=======================================
     * Getters and setters for the attributes
     =======================================*/
    //[Name]
    public String getName() {
        return roomName;   
    }

    public void setName(String name) {
        this.roomName = name;
    }
    
    //[Label
    public boolean isLabel() {
        return isRoomLabel;
    }

    public void setLabel(boolean roomLabel) {
        this.isRoomLabel = roomLabel;
    }

    public BoardCell getLabelCell() {
        return labelCell;
    }

    public void setLabelCell(BoardCell labelCell) {
        this.labelCell = labelCell;
    }
    //[Center]
    public boolean isRoomCenter() {
        return isRoomCenter;
    }

    public void setRoomCenter(boolean roomCenter) {
        this.isRoomCenter = roomCenter;
    }
    
    public BoardCell getCenterCell() {
        return centerCell;
    }

    public void setCenterCell(BoardCell centerCell) {
        this.centerCell = centerCell;
    }

    //[Secret Passage]
    public char getSecretPassage() {
        return secretPassage;
    }

    public void setSecretPassage(char secretPassage) {
        this.secretPassage = secretPassage;
    }
    
    public boolean isRoomSecret() {
		return isRoomSecret;
	}
    
	public void setRoomSecret(boolean isRoomSecret) {
		this.isRoomSecret = isRoomSecret;
	}

    //[Doors]
    public DoorDirection getDoorDirection() {
        return doorDirection;
    }

    public void setDoorDirection(DoorDirection doorDirection) {
        this.doorDirection = doorDirection;
    }
}