package in.kyle.ezskypeezlife.events.conversation.message;

import in.kyle.ezskypeezlife.api.conversation.SkypeConversation;
import in.kyle.ezskypeezlife.api.conversation.message.SkypeMessage;
import in.kyle.ezskypeezlife.api.user.SkypeUser;
import in.kyle.ezskypeezlife.events.SkypeEvent;
import in.kyle.ezskypeezlife.events.conversation.SkypeConversationEvent;
import in.kyle.ezskypeezlife.events.user.SkypeUserEvent;
import lombok.Data;

/**
 * Created by Kyle on 10/6/2015.
 */
@Data
public class SkypeMessageReceivedEvent implements SkypeEvent, SkypeUserEvent, SkypeConversationEvent, SkypeMessageEvent {
    
    private final SkypeMessage message;
    private final SkypeUser user;
    private final SkypeConversation conversation;
    
    /**
     * Sends a SkypeMessage back to the conversation where the message was received from
     * @param text - The message to send to the conversation
     * @return - The Skype message sent
     */
    public SkypeMessage reply(String text) {
        return message.getConversation().sendMessage(text);
    }
}
