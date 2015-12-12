package in.kyle.ezskypeezlife.events.conversation.message;

import in.kyle.ezskypeezlife.api.conversation.SkypeConversation;
import in.kyle.ezskypeezlife.api.conversation.message.SkypeMessage;
import in.kyle.ezskypeezlife.api.user.SkypeUser;
import in.kyle.ezskypeezlife.events.SkypeEvent;
import in.kyle.ezskypeezlife.events.conversation.SkypeConversationEvent;
import in.kyle.ezskypeezlife.events.user.SkypeUserEvent;
import lombok.Data;

/**
 * Created by Kyle on 10/27/2015.
 */
@Data
public class SkypeMessageEditedEvent implements SkypeEvent, SkypeUserEvent, SkypeConversationEvent, SkypeMessageEvent {
    
    private final SkypeUser user;
    private final SkypeConversation conversation;
    private final SkypeMessage message;
    private final String contentOld;
    private final String contentNew;
    
}
