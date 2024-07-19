package clueGame;
/*BadConfigFormatException
 * 
 * @author Ethan Eshbaugh
 * @author Colin Myers
 * 
 * Exception for bad configurations in config files
 * 
 */
public class BadConfigFormatException extends Exception{
	public BadConfigFormatException(String message) {
        super(message);
    }
}
