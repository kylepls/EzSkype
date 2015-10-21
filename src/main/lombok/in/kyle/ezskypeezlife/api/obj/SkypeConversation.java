package in.kyle.ezskypeezlife.api.obj;

import in.kyle.ezskypeezlife.api.SkypeConversationType;

import java.util.List;

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
     * Gets the URL to join this conversation
     * @return - The join URL
     */
    String getJoinUrl() throws Exception;
}
