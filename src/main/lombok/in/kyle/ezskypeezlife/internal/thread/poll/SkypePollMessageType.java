package in.kyle.ezskypeezlife.internal.thread.poll;

import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.api.conversation.message.SkypeMessageType;
import in.kyle.ezskypeezlife.internal.obj.SkypeConversationInternal;
import in.kyle.ezskypeezlife.internal.obj.SkypeMessageInternal;
import in.kyle.ezskypeezlife.internal.obj.SkypeUserInternal;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 * Created by Kyle on 10/9/2015.
 */
public abstract class SkypePollMessageType {
    
    public abstract boolean accept(String messageType);
    
    public abstract void extract(EzSkype ezSkype, JsonObject jsonObject, JsonObject resource) throws Exception;
    
    protected SkypeMessageInternal getMessageFromJson(EzSkype ezSkype, JsonObject jsonMessage) {
        JsonObject resource = jsonMessage.getAsJsonObject("resource");
    
        String id;
    
        if (resource.has("clientmessageid")) {
            id = resource.get("clientmessageid").getAsString();
        } else {
            id = resource.get("skypeeditedid").getAsString();
        }
    
        String longId = getConversationLongId(resource);
        
        SkypeUserInternal sender = getFromUser(ezSkype, resource);
    
        SkypeMessageType messageType = SkypeMessageType.getType(resource.get("messagetype").getAsString().toUpperCase());
        String content = StringEscapeUtils.unescapeXml(resource.get("content").getAsString());
        
        SkypeConversationInternal conversation = (SkypeConversationInternal) ezSkype.getSkypeConversation(longId);
        if (!conversation.isFullyLoaded()) {
            conversation.fullyLoad();
        }
        
        return new SkypeMessageInternal(ezSkype, id, sender, false, messageType, content, conversation);
    }
    
    protected String getConversationLongId(JsonObject resource) {
        String conversationLink = resource.get("conversationLink").getAsString();
        return conversationLink.substring(conversationLink.lastIndexOf("/") + 1);
    }
    
    protected SkypeUserInternal getFromUser(EzSkype ezSkype, JsonObject resource) {
        String senderUsername = resource.get("from").getAsString();
        senderUsername = senderUsername.substring(senderUsername.indexOf("8:") + 2);
        return (SkypeUserInternal) ezSkype.getSkypeUser(senderUsername);
    }
}
