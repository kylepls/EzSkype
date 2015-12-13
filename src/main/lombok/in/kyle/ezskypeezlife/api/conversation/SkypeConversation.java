package in.kyle.ezskypeezlife.api.conversation;

import in.kyle.ezskypeezlife.api.conversation.message.SkypeMessage;
import in.kyle.ezskypeezlife.api.user.SkypeUser;
import in.kyle.ezskypeezlife.api.user.SkypeUserRole;
import in.kyle.ezskypeezlife.events.conversation.SkypeConversationActionDeniedException;

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
     *
     * @param message - The message to send
     * @return - The sent message
     */
    SkypeMessage sendMessage(String message);
    
    /**
     * Kicks a user from the conversation
     *
     * @param skypeUser - The user to kick
     * @return - If the user was kicked
     */
    boolean kick(SkypeUser skypeUser);
    
    /**
     * Checks if a user has admin privileges
     *
     * @param skypeUser - The user to check
     * @return - True if the user has admin privileges
     */
    boolean isAdmin(SkypeUser skypeUser);
    
    /**
     * Gets a Skype user from this conversation
     *
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
     *
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
    void setUserRole(SkypeUser skypeUser, SkypeUserRole role) throws SkypeConversationActionDeniedException;
    
    /**
     * Load all fields other than the ID
     */
    void fullyLoad();
    
    /**
     * @return - If the conversation has all fields loaded, if this returns false then you must call fullyLoad before accessing any of
     * the fields inside this object
     */
    boolean isLoaded();
    
    /**
     * Sends an image ping to this conversation
     *
     * @param image - The image to send
     * @throws Exception - If the image could not be sent
     */
    void sendImage(File image) throws Exception;
    
    /**
     * Sends an image ping to this conversation
     *
     * @param inputStream - And image inputstream
     * @throws Exception - If the image could not be sent
     */
    void sendImage(InputStream inputStream) throws Exception;
    
    /**
     * Sends an image ping to this conversation
     *
     * @param url - The URL of an image you would like to ping
     * @throws Exception - If the image could not be sent
     */
    void sendImage(URL url) throws Exception;
    
}
