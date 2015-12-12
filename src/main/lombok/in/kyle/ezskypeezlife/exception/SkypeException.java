package in.kyle.ezskypeezlife.exception;

/**
 * Created by Kyle on 11/6/2015.
 */
public class SkypeException extends Exception {
    
    public SkypeException() {
    }
    
    public SkypeException(String message) {
        super(message);
    }
    
    public SkypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
