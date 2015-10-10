package in.kyle.ezskypeezlife.internal.obj;

import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.api.SkypeConversationPermission;
import in.kyle.ezskypeezlife.api.SkypeConversationType;
import in.kyle.ezskypeezlife.api.SkypeUserRole;
import in.kyle.ezskypeezlife.internal.packet.conversation.SkypeConversationAddPacket;
import in.kyle.ezskypeezlife.internal.packet.conversation.SkypeConversationKickPacket;
import in.kyle.ezskypeezlife.internal.packet.conversation.SkypeConversationRolePacket;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Created by Kyle on 10/7/2015.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SkypeGroupConversationInternal extends SkypeConversationInternal {
    
    private final List<SkypeConversationPermission> permissions;
    private final boolean joinEnabled;
    private final SkypeUserInternal creator;
    private final List<SkypeUserInternal> admins;
    
    // @formatter:off
    public SkypeGroupConversationInternal(
            EzSkype ezSkype,
            String longId,
            String topic,
            boolean historyEnabled,
            List<SkypeConversationPermission> permissions,
            boolean joinEnabled,
            SkypeUserInternal creator, 
            String url, 
            List<SkypeUserInternal> users,
            List<SkypeUserInternal> admins) {
        super(ezSkype, longId, topic, historyEnabled, joinEnabled, url, SkypeConversationType.GROUP, users);
        this.permissions = permissions;
        this.joinEnabled = joinEnabled;
        this.creator = creator;
        this.users = users;
        this.admins = admins;
    }
    // @formatter:on
    
    public void setRole(SkypeUserInternal skypeUserInternal, SkypeUserRole skypeUserRole) {
        new SkypeConversationRolePacket(ezSkype, longId, skypeUserInternal.getUsername(), skypeUserRole).executeAsync();
    }
    
    public SkypeUserRole getSkypeRole(SkypeUserInternal skypeUserInternal) {
        return admins.contains(skypeUserInternal) ? SkypeUserRole.ADMIN : SkypeUserRole.USER;
    }
    
    public void kick(SkypeUserInternal skypeUserInternal) {
        new SkypeConversationKickPacket(ezSkype, longId, skypeUserInternal.getUsername()).executeAsync();
    }
    
    public void add(SkypeUserInternal skypeUserInternal) {
        new SkypeConversationAddPacket(ezSkype, longId, skypeUserInternal.getUsername()).executeAsync();
    }
}
