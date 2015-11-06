package in.kyle.ezskypeezlife.events.conversation;

import in.kyle.ezskypeezlife.api.SkypeUserRole;
import in.kyle.ezskypeezlife.api.obj.SkypeConversation;
import in.kyle.ezskypeezlife.api.obj.SkypeUser;
import in.kyle.ezskypeezlife.events.SkypeEvent;
import lombok.Data;

/**
 * Created by Kyle on 10/8/2015.
 */
@Data
public class SkypeConversationUserRoleUpdate implements SkypeEvent {
    
    private final SkypeConversation conversation;
    private final SkypeUser user;
    private final SkypeUserRole oldRole;
    private final SkypeUserRole newRole;
    
}
