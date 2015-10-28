package in.kyle.ezskypeezlife.internal.obj;

import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.api.SkypeConversationType;
import in.kyle.ezskypeezlife.api.SkypeMessageType;
import in.kyle.ezskypeezlife.api.SkypeUserRole;
import in.kyle.ezskypeezlife.api.obj.SkypeConversation;
import in.kyle.ezskypeezlife.api.obj.SkypeUser;
import in.kyle.ezskypeezlife.internal.packet.conversation.SkypeConversationAddPacket;
import in.kyle.ezskypeezlife.internal.packet.conversation.SkypeConversationRolePacket;
import in.kyle.ezskypeezlife.internal.packet.conversation.SkypeConversationTopicPacket;
import in.kyle.ezskypeezlife.internal.packet.messages.SkypeSendMessagePacket;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Optional;

/**
 * Created by Kyle on 10/7/2015.
 */
@Data
@AllArgsConstructor
public abstract class SkypeConversationInternal implements SkypeConversation {
    
    protected EzSkype ezSkype;
    protected final SkypeMessageCacheInternal messageCache = new SkypeMessageCacheInternal();
    protected final String longId;
    protected String topic;
    protected boolean historyEnabled;
    protected boolean joinEnabled;
    protected String pictureUrl;
    protected final SkypeConversationType conversationType;
    protected List<SkypeUserInternal> users;
    
    public SkypeMessageInternal sendMessage(String message) {
        EzSkype.LOGGER.debug("Sending message \"{}\" to {}", message, longId);
        String id = Long.toString(System.currentTimeMillis()); // TODO maybe
        new SkypeSendMessagePacket(ezSkype, longId, message, id).executeAsync();
        return new SkypeMessageInternal(ezSkype, id, ezSkype.getLocalUser(), false, SkypeMessageType.RICHTEXT, message, this);
    }
    
    @Override
    public List<SkypeUser> getUsers() {
        return (List<SkypeUser>) (Object) users;
    }
    
    @Override
    public void addUser(SkypeUser skypeUser) {
        new SkypeConversationAddPacket(ezSkype, longId, skypeUser.getUsername()).executeAsync();
    }
    
    @Override
    public Optional<SkypeUser> getUser(String username) {
        return getUsers().stream().filter(skypeUser -> skypeUser.getUsername().equals(username)).findAny();
    }
    
    @Override
    public void changeTopic(String topic) {
        new SkypeConversationTopicPacket(ezSkype, longId, topic).executeAsync();
    }
    
    @Override
    public void setUserRole(SkypeUser skypeUser, SkypeUserRole role) {
        new SkypeConversationRolePacket(ezSkype, longId, skypeUser.getUsername(), role).executeAsync();
    }
}
