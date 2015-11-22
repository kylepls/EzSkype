package in.kyle.ezskypeezlife.events.conversation;

import in.kyle.ezskypeezlife.api.obj.SkypeConversation;
import in.kyle.ezskypeezlife.api.obj.SkypeUser;
import in.kyle.ezskypeezlife.events.SkypeConversationEvent;
import in.kyle.ezskypeezlife.events.SkypeEvent;
import in.kyle.ezskypeezlife.events.SkypeUserEvent;
import lombok.Data;

/**
 * Created by Kyle on 10/8/2015.
 */
@Data
public class SkypeConversationUpdateTopicEvent implements SkypeEvent, SkypeUserEvent, SkypeConversationEvent {
    
    private final SkypeConversation conversation;
    private final SkypeUser user;
    private final String oldTopic;
    private final String newTopic;
    
}
