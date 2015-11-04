package in.kyle.ezskypeezlife.api.obj;

import in.kyle.ezskypeezlife.api.SkypeConversationType;
import in.kyle.ezskypeezlife.api.SkypeUserRole;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Optional;

/**
 * Created by Kyle on 10/10/2015.
 */
public interface SkypeConversation {
    
    /**
     * 
     * Gets the conversation id
     * The id should look like this
     * "19:3000ebdcfcca4b42b9f6964f4066e1ad@thread.skype"
     * "8:username"
     *
     * @return - The conversation id
     */
    String getLongId();
    
    /**
     * @return - The conversation topic
     */
    String getTopic();
    
    /**
     * @return - If loading conversation history loading is enabled 
     */
    boolean isHistoryEnabled();
    
    /**
     * @return - If conversation joining is enabled
     */
    boolean isJoinEnabled();
    
    /**
     * @return - The conversation picture url
     */
    String getPictureUrl();
    
    /**
     * @return If the conversation is a group or user conversation
     */
    SkypeConversationType getConversationType();
    
    /**
     * @return - A list of users in the conversation
     */
    List<SkypeUser> getUsers();
    
    /**
     * Sends a message to the conversation
     * @param message - The message to send
     * @return - The message send
     */
    SkypeMessage sendMessage(String message);
    
    /**
     * Kicks a user from the conversation
     * @param skypeUser - The user to kick
     * @return - If the user was kicked
     */
    boolean kick(SkypeUser skypeUser);
    
    /**
     * Checks if a user has admin privileges
     * @param skypeUser - The user to check
     * @return - True if the user has admin privileges
     */
    boolean isAdmin(SkypeUser skypeUser);
    
    /**
     * Gets a Skype user from this conversation
     * @param username - The username of the user
     * @return - The user
     */
    Optional<SkypeUser> getUser(String username);
    
    /**
     * Adds a user to the conversation
     *
     * @param skypeUser - The user to add
     */
    void addUser(SkypeUser skypeUser);
    
    /**
     * Gets the URL to join this conversation
     * @return - The join URL
     */
    String getJoinUrl() throws Exception;
    
    /**
     * Sets the conversation topic
     *
     * @param topic - The conversation topic
     */
    void changeTopic(String topic);
    
    /**
     * Sets a users role
     *
     * @param skypeUser - The user to set role
     * @param role      - The role of the user
     */
    void setUserRole(SkypeUser skypeUser, SkypeUserRole role);
    
    /**
     * // TODO
     */
    void fullyLoad();
    
    /**
     * @return
     */
    boolean isLoaded();
    
    void sendImage(File image) throws Exception;
    
    void sendImage(InputStream inputStream) throws Exception;
    
    void sendImage(URL url) throws Exception;
}
