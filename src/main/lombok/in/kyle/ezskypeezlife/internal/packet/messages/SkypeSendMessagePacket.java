package in.kyle.ezskypeezlife.internal.packet.messages;

import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.internal.packet.HTTPRequest;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;
import lombok.ToString;

/**
 * Created by Kyle on 10/5/2015.
 * <p>
 * Used to send a Skype message to a user
 */
@ToString
public class SkypeSendMessagePacket extends SkypePacket {
    
    private String message;
    private String id;
    
    /**
     * @param ezSkype - Skype instance
     * @param convoId - The conversation id of the message
     * @param message - The message content
     */
    public SkypeSendMessagePacket(EzSkype ezSkype, String convoId, String message, String id) {
        super("https://client-s.gateway.messenger.live.com/v1/users/ME/conversations/" + convoId + "/messages", HTTPRequest.POST, 
                ezSkype, true);
        this.message = message;
        this.id = id;
    }
    
    @Override
    protected JsonObject run(WebConnectionBuilder webConnectionBuilder) throws Exception {
        JsonObject data = new JsonObject();
        data.addProperty("content", message);
        data.addProperty("messagetype", "RichText");
        data.addProperty("contenttype", "text");
        data.addProperty("clientmessageid", id);
        webConnectionBuilder.setPostData(data.toString());
        webConnectionBuilder.setTimeout(5000);
        webConnectionBuilder.send();
        return data;
    }
}
