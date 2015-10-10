package in.kyle.ezskypeezlife.internal.thread;

import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.internal.obj.SkypeConversationInternal;
import in.kyle.ezskypeezlife.internal.obj.SkypeMessageInternal;
import in.kyle.ezskypeezlife.internal.obj.SkypeUserInternal;

/**
 * Created by Kyle on 10/9/2015.
 */
public abstract class SkypePollMessageType {
    
    public abstract boolean accept(String messageType);
    
    public abstract void extract(EzSkype ezSkype, JsonObject jsonObject, JsonObject resource) throws Exception;
    
    protected SkypeMessageInternal getMessageFromJson(EzSkype ezSkype, JsonObject jsonMessage) throws Exception {
        JsonObject resource = jsonMessage.getAsJsonObject("resource");
        
        String id = resource.get("clientmessageid").getAsString();
        String conversationLink = resource.get("conversationLink").getAsString();
        String longId = getConversationLongId(resource);
        
        SkypeUserInternal sender = getFromUser(ezSkype, resource);
        
        in.kyle.ezskypeezlife.api.SkypeMessageType messageType = in.kyle.ezskypeezlife.api.SkypeMessageType.valueOf(resource.get
                ("messagetype").getAsString().toUpperCase());
        String content = resource.get("content").getAsString();
        
        SkypeConversationInternal conversation = ezSkype.getSkypeConversation(longId);
        
        // TODO fix params
        
        return new SkypeMessageInternal(ezSkype, id, sender, false, messageType, content, conversation);
    }
    
    protected String getConversationLongId(JsonObject resource) {
        String conversationLink = resource.get("conversationLink").getAsString();
        return conversationLink.substring(conversationLink.lastIndexOf("/") + 1);
    }
    
    protected SkypeUserInternal getFromUser(EzSkype ezSkype, JsonObject resource) {
        String senderUsername = resource.get("from").getAsString();
        senderUsername = senderUsername.substring(senderUsername.indexOf("8:") + 2);
        return ezSkype.getSkypeUser(senderUsername);
    }
}
