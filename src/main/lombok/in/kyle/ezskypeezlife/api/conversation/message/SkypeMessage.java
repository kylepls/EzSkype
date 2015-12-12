package in.kyle.ezskypeezlife.api.conversation.message;

import in.kyle.ezskypeezlife.api.conversation.SkypeConversation;
import in.kyle.ezskypeezlife.api.user.SkypeUser;

/**
 * Created by Kyle on 10/6/2015.
 */
public interface SkypeMessage {
    
    String getId();
    
    String getMessage();
    
    boolean isEdited();
    
    SkypeUser getSender();
    
    SkypeMessageType getMessageType();
    
    SkypeConversation getConversation();
    
    void edit(String message);
}
