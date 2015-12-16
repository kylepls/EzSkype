package in.kyle.ezskypeezlife.internal.obj;

import com.jamesmurty.utils.XMLBuilder;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.api.conversation.SkypeConversation;
import in.kyle.ezskypeezlife.api.conversation.SkypeConversationType;
import in.kyle.ezskypeezlife.api.conversation.message.SkypeFlik;
import in.kyle.ezskypeezlife.api.conversation.message.SkypeMessageType;
import in.kyle.ezskypeezlife.api.user.SkypeUser;
import in.kyle.ezskypeezlife.api.user.SkypeUserRole;
import in.kyle.ezskypeezlife.events.conversation.SkypeConversationActionDeniedException;
import in.kyle.ezskypeezlife.exception.SkypeException;
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

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    
    public void sendFlik(SkypeFlik skypeFlik) throws ParserConfigurationException, TransformerException {
        String id = skypeFlik.getId();
        EzSkype.LOGGER.debug("Sending flik {} to {}", skypeFlik, longId);
        // @formatter:off
        String content = 
                XMLBuilder.create("URIObject").a("type", "Video.1/Flik.1").a("uri", "https://static.asm.skype.com/pes/v1/items/" + id)
                    .a("url_thumbnail", "https://static.asm.skype.com/pes/v1/items/" + id + "/views/thumbnail")
                        .e("a").a("href", "https://static.asm.skype.com/pes/v1/items/" + id + "/views/default")
                            .t("https://static.asm.skype.com/pes/v1/items/" + id + "/views/default")
                    .up()
                        .e("OriginalName").a("v", "")
                    .up().asString();
        // @formatter:on
        
        new SkypeSendMessagePacket(ezSkype, longId, content, Long.toString(ezSkype.getMessageId().incrementAndGet()), SkypeMessageType
                .MEDIA_FLIKMSG).executeAsync();
    }
    
    public void sendMessage(String content, SkypeMessageType skypeMessageType) {
        String id = Long.toString(ezSkype.getMessageId().incrementAndGet());
        new SkypeSendMessagePacket(ezSkype, longId, content, id, skypeMessageType).executeAsync();
    }
    
    public SkypeMessageInternal sendMessage(String message) {
        EzSkype.LOGGER.debug("Sending message \"{}\" to {}", message, longId);
        String id = Long.toString(ezSkype.getMessageId().incrementAndGet());
        new SkypeSendMessagePacket(ezSkype, longId, message, id, SkypeMessageType.RICH_TEXT).executeAsync();
        return new SkypeMessageInternal(ezSkype, id, (SkypeUserInternal) ezSkype.getLocalUser(), false, SkypeMessageType.RICH_TEXT, 
                message, this);
    }
    
    
    @Override
    public List<SkypeUser> getUsers() {
        return (List) users;
    }
    
    @Override
    public void addUser(SkypeUser skypeUser) {
        // TODO check topic
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
    public void setUserRole(SkypeUser skypeUser, SkypeUserRole role) throws SkypeConversationActionDeniedException {
        if (isAdmin(ezSkype.getLocalUser())) {
            new SkypeConversationRolePacket(ezSkype, longId, skypeUser.getUsername(), role).executeAsync();
        } else {
            throw new SkypeConversationActionDeniedException("You must be a chat admin to perform this action");
        }
    }
    
    @Override
    public void sendImage(File image) throws Exception {
        sendImage(new FileInputStream(image));
    }
    
    @Override
    public void sendImage(URL url) throws Exception {
        sendImage(url.openStream());
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
    public void loadAllUsers() throws IOException, SkypeException {
        ezSkype.loadAll(users.stream().map(SkypeUserInternal::getUsername).collect(Collectors.toList()));
    }
}
