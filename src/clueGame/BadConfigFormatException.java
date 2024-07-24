package clueGame;
/*BadConfigFormatException
 * 
 * @author Ethan Eshbaugh
 * @author Colin Myers
 * 
 * Exception for bad configurations in config files when initializing board
 * 
 */

//auto suggested fix to get rid of little yellow error for class ID, didn't like it there
@SuppressWarnings("serial")
public class BadConfigFormatException extends Exception{

	//generic constructor
	public BadConfigFormatException() {
		super();
	}
	
	//custom constructor
	public BadConfigFormatException(String message) {
        super(message);
    }
	
}
