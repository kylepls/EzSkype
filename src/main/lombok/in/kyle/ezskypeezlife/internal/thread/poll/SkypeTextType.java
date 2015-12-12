package in.kyle.ezskypeezlife.internal.thread.poll;

import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.events.conversation.message.SkypeMessageEditedEvent;
import in.kyle.ezskypeezlife.events.conversation.message.SkypeMessageReceivedEvent;
import in.kyle.ezskypeezlife.internal.obj.SkypeConversationInternal;
import in.kyle.ezskypeezlife.internal.obj.SkypeMessageInternal;

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
            SkypeMessageReceivedEvent skypeMessageReceivedEvent = new SkypeMessageReceivedEvent(skypeMessageInternal, 
                    skypeMessageInternal.getSender(), skypeMessageInternal.getConversation());
            ezSkype.getEventManager().fire(skypeMessageReceivedEvent);
        } else {
            SkypeMessageInternal messageNew = getMessageFromJson(ezSkype, jsonObject);
    
            String msgText = messageNew.getMessage();
            String search = "Edited previous message: ";
    
            int searchIndex = msgText.indexOf(search);
    
            if (searchIndex < 0) {
                return;
            }
    
            msgText = msgText.substring(searchIndex + search.length()).trim();
    
            if (msgText.contains("<e_m")) {
                msgText = msgText.substring(0, msgText.indexOf("<e_m"));
            }
    
            SkypeConversationInternal conversation = messageNew.getConversation();
            SkypeMessageInternal oldMessage = conversation.getMessageCache().getSkypeMessage(messageNew.getId());
    
            if (oldMessage == null) {
                oldMessage = messageNew;
            }
    
            String oldMessageContent = oldMessage.getMessage();
    
            oldMessage.setEdited(true);
            oldMessage.setMessage(msgText);
    
            SkypeMessageEditedEvent skypeMessageEditedEvent = new SkypeMessageEditedEvent(oldMessage.getSender(), conversation, 
                    oldMessage, oldMessageContent, msgText);
            ezSkype.getEventManager().fire(skypeMessageEditedEvent);
        }
    }
}
