package in.kyle.ezskypeezlife.api.errors;

/**
 * Created by Kyle on 11/5/2015.
 */
public interface SkypeErrorHandler {
    
    /**
     * Returns a result to the given errors
     *
     * @param skypeCaptcha - The errors to sole
     * @return - The solution to the errors
     */
    String solve(SkypeCaptcha skypeCaptcha);
    
    /**
     * Sets a new password for the user, this is called when Skype requires a user to change their password
     * If you do not want to change your password, return an empty string or null
     *
     * @return - The new Skype password
     */
    String setNewPassword();
    
    /**
     * Called whenever an exception is thrown when sending a packet and whatnot
     */
    void handleException(Exception exception);
}
