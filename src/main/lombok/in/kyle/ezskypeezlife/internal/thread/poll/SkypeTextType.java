package in.kyle.ezskypeezlife.internal.thread.poll;

import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.events.conversation.SkypeMessageReceivedEvent;
import in.kyle.ezskypeezlife.internal.obj.SkypeMessageInternal;
import in.kyle.ezskypeezlife.internal.thread.SkypePollMessageType;

/**
 * Created by Kyle on 10/9/2015.
 */
public class SkypeTextType extends SkypePollMessageType {
    
    @Override
    public boolean accept(String messageType) {
        return messageType.equals("Text") || messageType.equals("RichText");
    }
    
    @Override
    public void extract(EzSkype ezSkype, JsonObject jsonObject, JsonObject resource) throws Exception {
        if (resource.has("clientmessageid")) { // new message
            SkypeMessageInternal skypeMessageInternal = getMessageFromJson(ezSkype, jsonObject);
            SkypeMessageReceivedEvent skypeMessageReceivedEvent = new SkypeMessageReceivedEvent(skypeMessageInternal);
            ezSkype.getEventManager().fire(skypeMessageReceivedEvent);
    
            System.out.println("Got message: " + Thread.currentThread().getName());
            
        } else {
            // TODO message edit
        }
    }
}
