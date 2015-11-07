package in.kyle.ezskypeezlife.internal.packet.conversation;

import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.internal.packet.ContentType;
import in.kyle.ezskypeezlife.internal.packet.HTTPRequest;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;

/**
 * Created by Kyle on 10/25/2015.
 */
public class SkypeConversationTopicPacket extends SkypePacket {
    
    private final String topic;
    
    public SkypeConversationTopicPacket(EzSkype ezSkype, String longId, String topic) {
        super("https://client-s.gateway.messenger.live.com/v1/threads/" + longId + "/properties?name=topic", HTTPRequest.PUT, ezSkype, 
                true);
        this.topic = topic;
    }
    
    @Override
    protected Object run(WebConnectionBuilder webConnectionBuilder) throws Exception {
        
        JsonObject data = new JsonObject();
        data.addProperty("topic", topic);
        
        webConnectionBuilder.setPostData(data.toString());
        webConnectionBuilder.setContentType(ContentType.JSON);
        webConnectionBuilder.send();
        return null;
    }
}
