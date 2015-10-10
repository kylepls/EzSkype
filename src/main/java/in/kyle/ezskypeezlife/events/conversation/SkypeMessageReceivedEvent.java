package in.kyle.ezskypeezlife.events.conversation;

import in.kyle.ezskypeezlife.api.obj.SkypeMessage;
import in.kyle.ezskypeezlife.events.SkypeEvent;
import lombok.Data;

/**
 * Created by Kyle on 10/6/2015.
 */
@Data
public class SkypeMessageReceivedEvent implements SkypeEvent {
    
    private final SkypeMessage message;
    
}
