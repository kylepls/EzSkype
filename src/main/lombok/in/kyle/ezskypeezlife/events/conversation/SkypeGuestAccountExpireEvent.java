package in.kyle.ezskypeezlife.events.conversation;

import in.kyle.ezskypeezlife.api.obj.SkypeConversation;
import in.kyle.ezskypeezlife.api.obj.SkypeUser;
import in.kyle.ezskypeezlife.events.SkypeEvent;
import lombok.Data;

/**
 * Created by Kyle on 10/27/2015.
 */
@Data
public class SkypeGuestAccountExpireEvent implements SkypeEvent {
    
    private final SkypeUser skypeUser;
    private final SkypeConversation skypeConversation;
    
}
