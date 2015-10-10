package in.kyle.ezskypeezlife.events.conversation;

import in.kyle.ezskypeezlife.api.obj.SkypeUser;
import in.kyle.ezskypeezlife.events.SkypeEvent;
import lombok.Data;

/**
 * Created by Kyle on 10/9/2015.
 */
@Data
public class SkypeContactAuthorizeEvent implements SkypeEvent {
    
    private final SkypeUser user;
    
}
