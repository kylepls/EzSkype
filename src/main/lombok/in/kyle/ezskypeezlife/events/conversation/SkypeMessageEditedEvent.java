package in.kyle.ezskypeezlife.events.conversation;

import in.kyle.ezskypeezlife.api.obj.SkypeConversation;
import in.kyle.ezskypeezlife.api.obj.SkypeMessage;
import in.kyle.ezskypeezlife.api.obj.SkypeUser;
import in.kyle.ezskypeezlife.events.SkypeConversationEvent;
import in.kyle.ezskypeezlife.events.SkypeEvent;
import in.kyle.ezskypeezlife.events.SkypeMessageEvent;
import in.kyle.ezskypeezlife.events.SkypeUserEvent;
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
