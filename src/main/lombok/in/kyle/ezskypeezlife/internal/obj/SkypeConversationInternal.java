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
import in.kyle.ezskypeezlife.internal.packet.messages.SkypeSendImagePacket;
import in.kyle.ezskypeezlife.internal.packet.messages.SkypeSendMessagePacket;
import in.kyle.ezskypeezlife.internal.packet.messages.image.SkypeGetImageIdPacket;
import in.kyle.ezskypeezlife.internal.packet.messages.image.SkypeSetImagePermissionsPacket;
import in.kyle.ezskypeezlife.internal.packet.messages.image.SkypeWriteImagePacket;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Optional;

/**
 * Created by Kyle on 10/7/2015.
 */
@Data
@AllArgsConstructor
public abstract class SkypeConversationInternal implements SkypeConversation {
    
    protected final SkypeMessageCacheInternal messageCache = new SkypeMessageCacheInternal();
    protected final String longId;
    protected final SkypeConversationType conversationType;
    protected EzSkype ezSkype;
    protected String topic;
    protected boolean historyEnabled;
    protected boolean joinEnabled;
    protected String pictureUrl;
    protected List<SkypeUserInternal> users;
    protected boolean fullyLoaded;
    
    @Override
    public List<SkypeUser> getUsers() {
        return (List<SkypeUser>) (Object) users;
    }
    
    public SkypeMessageInternal sendMessage(String message) {
        EzSkype.LOGGER.debug("Sending message \"{}\" to {}", message, longId);
        String id = Long.toString(ezSkype.getMessageId().incrementAndGet());
        new SkypeSendMessagePacket(ezSkype, longId, message, id).executeAsync();
        return new SkypeMessageInternal(ezSkype, id, (SkypeUserInternal) ezSkype.getLocalUser(), false, SkypeMessageType.RICHTEXT, 
                message, this);
    }
    
    @Override
    public Optional<SkypeUser> getUser(String username) {
        return getUsers().stream().filter(skypeUser -> skypeUser.getUsername().equals(username)).findAny();
    }
    
    @Override
    public void addUser(SkypeUser skypeUser) {
        new SkypeConversationAddPacket(ezSkype, longId, skypeUser.getUsername()).executeAsync();
    }
    
    @Override
    public void changeTopic(String topic) {
        new SkypeConversationTopicPacket(ezSkype, longId, topic).executeAsync();
    }
    
    @Override
    public void setUserRole(SkypeUser skypeUser, SkypeUserRole role) {
        new SkypeConversationRolePacket(ezSkype, longId, skypeUser.getUsername(), role).executeAsync();
    }
    
    @Override
    public void sendImage(File image) throws Exception {
        sendImage(new FileInputStream(image));
    }
    
    @Override
    public void sendImage(InputStream imageInputStream) throws Exception {
        SkypeGetImageIdPacket getImageIdPacket = new SkypeGetImageIdPacket(ezSkype);
        String imageId = (String) getImageIdPacket.executeSync();
    
        SkypeSetImagePermissionsPacket skypeSetImagePermissionsPacket = new SkypeSetImagePermissionsPacket(ezSkype, imageId, longId);
        skypeSetImagePermissionsPacket.executeSync();
    
        SkypeWriteImagePacket skypeWriteImagePacket = new SkypeWriteImagePacket(ezSkype, imageId, imageInputStream);
        skypeWriteImagePacket.executeSync();
    
        SkypeSendImagePacket skypeSendImagePacket = new SkypeSendImagePacket(ezSkype, longId, imageId, "asd.png");
        skypeSendImagePacket.executeSync();
    }
    
    @Override
    public void sendImage(URL url) throws Exception {
        sendImage(url.openStream());
    }
}
