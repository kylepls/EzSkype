package in.kyle.ezskypeezlife.internal.thread.poll;

import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.events.conversation.SkypeMessageEditedEvent;
import in.kyle.ezskypeezlife.events.conversation.SkypeMessageReceivedEvent;
import in.kyle.ezskypeezlife.internal.obj.SkypeConversationInternal;
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
        if (resource.has("clientmessageid")) {
            SkypeMessageInternal skypeMessageInternal = getMessageFromJson(ezSkype, jsonObject);
            skypeMessageInternal.getConversation().getMessageCache().addMessage(skypeMessageInternal);
            SkypeMessageReceivedEvent skypeMessageReceivedEvent = new SkypeMessageReceivedEvent(skypeMessageInternal);
            ezSkype.getEventManager().fire(skypeMessageReceivedEvent);
        } else {
            SkypeMessageInternal messageNew = getMessageFromJson(ezSkype, jsonObject);
    
            String msg = messageNew.getMessage();
            String search = "Edited previous message: ";
            msg = msg.substring(msg.indexOf(search) + search.length()).trim();
    
            if (msg.contains("<e_m")) {
                msg = msg.substring(0, msg.indexOf("<e_m"));
            }
    
            SkypeConversationInternal conversation = messageNew.getConversation();
            SkypeMessageInternal oldMessage = conversation.getMessageCache().getSkypeMessage(messageNew.getId());
    
            if (oldMessage == null) {
                oldMessage = messageNew;
            }
    
            String oldMessageContent = oldMessage.getMessage();
    
            oldMessage.setEdited(true);
            oldMessage.setMessage(msg);
    
            SkypeMessageEditedEvent skypeMessageEditedEvent = new SkypeMessageEditedEvent(oldMessage.getSender(), oldMessage, 
                    oldMessageContent, msg);
            ezSkype.getEventManager().fire(skypeMessageEditedEvent);
        }
    }
}
