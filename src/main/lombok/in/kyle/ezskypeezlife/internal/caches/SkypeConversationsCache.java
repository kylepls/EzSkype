package in.kyle.ezskypeezlife.internal.caches;

import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.api.SkypeConversationType;
import in.kyle.ezskypeezlife.internal.obj.SkypeConversationInternal;
import in.kyle.ezskypeezlife.internal.obj.SkypeConversationInternalEmpty;
import in.kyle.ezskypeezlife.internal.obj.SkypeUserConversationInternal;
import in.kyle.ezskypeezlife.internal.packet.conversation.SkypeGetGroupConversationPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Kyle on 10/7/2015.
 */
public class SkypeConversationsCache {
    
    private List<SkypeConversationInternal> skypeConversations;
    private EzSkype ezSkype;
    
    public SkypeConversationsCache(EzSkype ezSkype) {
        this.ezSkype = ezSkype;
        this.skypeConversations = new ArrayList<>();
    }
    
    /**
     * Get a conversation from the cache or get it from the server
     *
     * @param longId - The longId of the user
     * @return - The Skype user
     */
    public SkypeConversationInternal getSkypeConversation(String longId) {
        Optional<SkypeConversationInternal> convoOptional = skypeConversations.stream().filter(skypeConvo -> skypeConvo.getLongId().equals(longId)).findAny();
        if (!convoOptional.isPresent()) {
            SkypeConversationType conversationType = SkypeConversationType.fromLongId(longId);
            if (conversationType == SkypeConversationType.USER) {
                return new SkypeUserConversationInternal(ezSkype, longId, "Other " +
                        "user", true, false, "");
            } else {
                SkypeGetGroupConversationPacket getUserInfoPacket = new SkypeGetGroupConversationPacket(ezSkype, longId);
    
                try {
                    SkypeConversationInternal skypeConversationInternal = (SkypeConversationInternal) getUserInfoPacket.executeSync();
                    skypeConversations.add(skypeConversationInternal);
                    return skypeConversationInternal;
                } catch (Exception e) {
                    e.printStackTrace();
                    return new SkypeConversationInternalEmpty(ezSkype, longId);
                }
            }
        } else {
            return convoOptional.get();
        }
    }
    
    public List<SkypeConversationInternal> getSkypeConversations() {
        return skypeConversations;
    }
}
