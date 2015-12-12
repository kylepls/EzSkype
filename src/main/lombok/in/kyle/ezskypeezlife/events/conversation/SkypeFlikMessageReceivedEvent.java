package in.kyle.ezskypeezlife.events.conversation;

import in.kyle.ezskypeezlife.api.conversation.SkypeConversation;
import in.kyle.ezskypeezlife.api.conversation.message.SkypeFlikMessage;
import in.kyle.ezskypeezlife.api.user.SkypeUser;
import in.kyle.ezskypeezlife.events.conversation.message.SkypeMessageReceivedEvent;
import lombok.EqualsAndHashCode;

/**
 * Created by Kyle on 12/5/2015.
 */
@EqualsAndHashCode(callSuper = false)
public class SkypeFlikMessageReceivedEvent extends SkypeMessageReceivedEvent {
    
    public SkypeFlikMessageReceivedEvent(SkypeFlikMessage message, SkypeUser user, SkypeConversation conversation) {
        super(message, user, conversation);
    }
    
    @Override
    public SkypeFlikMessage getMessage() {
        return (SkypeFlikMessage) super.getMessage();
    }
}
