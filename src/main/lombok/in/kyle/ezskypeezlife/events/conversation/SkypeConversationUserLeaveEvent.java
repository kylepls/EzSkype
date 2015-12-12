package in.kyle.ezskypeezlife.events.conversation;

import in.kyle.ezskypeezlife.api.conversation.SkypeConversation;
import in.kyle.ezskypeezlife.api.user.SkypeUser;
import in.kyle.ezskypeezlife.events.SkypeEvent;
import in.kyle.ezskypeezlife.events.user.SkypeUserEvent;
import lombok.Data;

/**
 * Created by Kyle on 10/8/2015.
 */
@Data
public class SkypeConversationUserLeaveEvent implements SkypeEvent, SkypeUserEvent, SkypeConversationEvent {
    
    private final SkypeConversation conversation;
    private final SkypeUser user;
    
}
