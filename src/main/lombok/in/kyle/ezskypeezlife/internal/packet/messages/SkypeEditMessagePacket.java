package in.kyle.ezskypeezlife.internal.packet.messages;

import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.internal.packet.HTTPRequest;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;

import java.io.IOException;

/**
 * Created by Kyle on 10/7/2015.
 */
public class SkypeEditMessagePacket extends SkypePacket {
    
    private final String content;
    private final String messageId;
    
    public SkypeEditMessagePacket(EzSkype ezSkype, String conversationLongId, String content, String messageId) {
        super("https://client-s.gateway.messenger.live.com/v1/users/ME/conversations/{}/messages", HTTPRequest.POST, ezSkype, true, 
                conversationLongId);
        this.content = content;
        this.messageId = messageId;
    }
    
    @Override
    protected Object run(WebConnectionBuilder webConnectionBuilder) throws IOException {
        JsonObject data = new JsonObject();
        data.addProperty("content", content);
        data.addProperty("messagetype", "RichText");
        data.addProperty("contenttype", "text");
        data.addProperty("skypeeditedid", messageId);
        logger.debug("Sending edit: " + data.toString());
        webConnectionBuilder.setPostData(data.toString());
        webConnectionBuilder.send();
        return null;
    }
}
