package in.kyle.ezskypeezlife.api.obj;

import java.util.Optional;

/**
 * Created by Kyle on 10/10/2015.
 */
public interface SkypeUser {
    
    String getUsername();
    
    Optional<String> getFirstName();
    
    Optional<String> getLastName();
    
    /**
     * @return - Users nonhtml mood
     */
    Optional<String> getMood();
    
    /**
     * @return - Users HTML mood
     */
    Optional<String> getRichMood();
    
    Optional<String> getDisplayName();
    
    Optional<String> getCountry();
    
    Optional<String> getCity();
    
    /**
     * Gets the users avatar URL
     * Sometimes this URL can only be accessed when using the correct HTTP headers
     * If you are not allowed to view the picture try adding the RegistrationToken header
     * 
     * @return - The users avatar url
     */
    Optional<String> getAvatarUrl();
    
    /**
     * @return - If the user is a contact
     */
    boolean isContact();
    
    /**
     * @return - If the user has been blocked
     */
    boolean isBlocked();
    
    /**
     * Adds the user as a contact
     * If contact == true
     *  This will send both a contact request packet and a contact accept packet for simplicity
     * Else
     *  This will send the contact delete packet
     * 
     * @param contact - Add/remove this user as a contact
     */
    void setContact(boolean contact);
    
    /**
     * Send a Skype message to the user
     * @param message - The message to send to the user
     * @return - The Skype message sent to the user
     */
    SkypeMessage sendMessage(String message);
    
    /**
     * Checks if the Skype user has been loaded from the Skype server
     * If the user has not been loaded from the server then some user fields may not be present
     * 
     * @return - If the user has been loaded
     */
    boolean isFullyLoaded();
    
    /**
     * Updates the user information from the server
     * This is done on the main thread
     */
    void fullyLoad();
}
