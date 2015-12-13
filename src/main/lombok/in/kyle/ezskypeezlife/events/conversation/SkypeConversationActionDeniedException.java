package in.kyle.ezskypeezlife.events.conversation;

import in.kyle.ezskypeezlife.exception.SkypeException;

/**
 * Created by Kyle on 12/13/2015.
 */
public class SkypeConversationActionDeniedException extends SkypeException {
    
    public SkypeConversationActionDeniedException(String message) {
        super(message);
    }
}
