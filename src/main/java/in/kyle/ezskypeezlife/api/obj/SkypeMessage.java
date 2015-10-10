package in.kyle.ezskypeezlife.api.obj;

import in.kyle.ezskypeezlife.api.SkypeMessageType;

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
