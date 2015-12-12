package in.kyle.ezskypeezlife.events.conversation;

import in.kyle.ezskypeezlife.api.obj.SkypeConversation;
import in.kyle.ezskypeezlife.api.obj.SkypeMessage;
import in.kyle.ezskypeezlife.api.obj.SkypeUser;
import in.kyle.ezskypeezlife.events.SkypeConversationEvent;
import in.kyle.ezskypeezlife.events.SkypeEvent;
import in.kyle.ezskypeezlife.events.SkypeUserEvent;
import lombok.Data;

/**
 * Created by Kyle on 10/6/2015.
 */
@Data
public class SkypeMessageReceivedEvent implements SkypeEvent, SkypeUserEvent, SkypeConversationEvent {
    
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
