package in.kyle.ezskypeezlife.events.conversation;

import in.kyle.ezskypeezlife.api.obj.SkypeConversation;
import in.kyle.ezskypeezlife.api.obj.SkypeUser;
import in.kyle.ezskypeezlife.events.SkypeEvent;
import lombok.Data;

/**
 * Created by Kyle on 10/8/2015.
 */
@Data
public class SkypeConversationCallEndedEvent implements SkypeEvent {
    
    private final SkypeUser user;
    private final SkypeConversation conversation;
    
}
