package in.kyle.ezskypeezlife.internal.obj;

import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.api.conversation.message.SkypeMessage;
import in.kyle.ezskypeezlife.api.conversation.message.SkypeMessageType;
import in.kyle.ezskypeezlife.internal.packet.messages.SkypeEditMessagePacket;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by Kyle on 10/6/2015.
 */
@AllArgsConstructor
@Data
public class SkypeMessageInternal implements SkypeMessage {
    
    private EzSkype ezSkype;
    private String id;
    private SkypeUserInternal sender;
    private boolean edited;
    private SkypeMessageType messageType;
    private String message;
    private SkypeConversationInternal conversation;
    
    public void edit(String message) {
        new SkypeEditMessagePacket(ezSkype, conversation.getLongId(), message, id).executeAsync();
        this.message = message;
        this.edited = true;
    }
    
    public String toString() {
        return message;
    }
}
