package in.kyle.ezskypeezlife.events;

import in.kyle.ezskypeezlife.api.obj.SkypeConversation;

/**
 * Created by Kyle on 11/21/2015.
 * <p>
 * And event that is called when an event takes place in a Skype conversation
 */
public interface SkypeConversationEvent {
    
    SkypeConversation getConversation();
    
}
