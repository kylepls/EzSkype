package in.kyle.ezskypeezlife.events.conversation;

import in.kyle.ezskypeezlife.api.obj.SkypeMessage;
import in.kyle.ezskypeezlife.api.obj.SkypeUser;
import in.kyle.ezskypeezlife.events.SkypeEvent;
import lombok.Data;

/**
 * Created by Kyle on 10/27/2015.
 */
@Data
public class SkypeMessageEditedEvent implements SkypeEvent {
    
    private final SkypeUser skypeUser;
    
    private final SkypeMessage skypeMessage;
    
    private final String contentOld;
    
    private final String contentNew;
    
}
