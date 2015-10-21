package in.kyle.ezskypeezlife.internal.obj;

import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.api.SkypeConversationType;
import in.kyle.ezskypeezlife.api.SkypeMessageType;
import in.kyle.ezskypeezlife.api.obj.SkypeConversation;
import in.kyle.ezskypeezlife.api.obj.SkypeUser;
import in.kyle.ezskypeezlife.internal.packet.messages.SkypeSendMessagePacket;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Created by Kyle on 10/7/2015.
 */
@Data
@AllArgsConstructor
public abstract class SkypeConversationInternal implements SkypeConversation {
    
    protected EzSkype ezSkype;
    protected String longId;
    protected String topic;
    protected boolean historyEnabled;
    protected boolean joinEnabled;
    protected String pictureUrl;
    protected SkypeConversationType conversationType;
    protected List<SkypeUserInternal> users;
    
    public SkypeMessageInternal sendMessage(String message) {
        EzSkype.LOGGER.debug("Sending message \"{}\" to {}", message, longId);
        String id = Long.toString(System.currentTimeMillis());
        new SkypeSendMessagePacket(ezSkype, longId, message, id).executeAsync();
        return new SkypeMessageInternal(ezSkype, id, ezSkype.getLocalUser(), false, SkypeMessageType.RICHTEXT, message, this);
    }
    
    @Override
    public List<SkypeUser> getUsers() {
        return (List<SkypeUser>) (Object) users;
    }
}
