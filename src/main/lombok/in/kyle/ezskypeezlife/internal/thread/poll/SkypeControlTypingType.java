package in.kyle.ezskypeezlife.internal.thread.poll;

import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.events.conversation.SkypeUserTypingEvent;
import in.kyle.ezskypeezlife.internal.obj.SkypeConversationInternal;
import in.kyle.ezskypeezlife.internal.obj.SkypeUserInternal;

/**
 * Created by Kyle on 10/9/2015.
 */
public class SkypeControlTypingType extends SkypePollMessageType {
    
    @Override
    public boolean accept(String messageType) {
        return messageType.equals("Control/Typing");
    }
    
    @Override
    public void extract(EzSkype ezSkype, JsonObject jsonObject, JsonObject resource) throws Exception {
        String longId = getConversationLongId(resource);
        SkypeConversationInternal conversation = (SkypeConversationInternal) ezSkype.getSkypeConversation(longId);
        SkypeUserInternal from = getFromUser(ezSkype, resource);
        SkypeUserTypingEvent userTypingEvent = new SkypeUserTypingEvent(from, conversation);
        ezSkype.getEventManager().fire(userTypingEvent);
    }
}
