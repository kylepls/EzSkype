package in.kyle.ezskypeezlife.internal.packet.conversation;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.api.SkypeConversationType;
import in.kyle.ezskypeezlife.internal.obj.SkypeConversationInternal;
import in.kyle.ezskypeezlife.internal.obj.SkypeGroupConversationInternal;
import in.kyle.ezskypeezlife.internal.obj.SkypeUserConversationInternal;
import in.kyle.ezskypeezlife.internal.packet.HTTPRequest;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kyle on 10/8/2015.
 */
public class SkypeGetConversationsPacket extends SkypePacket {
    
    private static final String URL = "https://client-s.gateway.messenger.live" + "" +
            ".com/v1/users/ME/conversations?startTime=0&pageSize=200&view=msnp24Equivalent&targetType=Passport|Skype|Lync|Thread";
    
    public SkypeGetConversationsPacket(EzSkype ezSkype) {
        super(URL, HTTPRequest.GET, ezSkype, true);
    }
    
    @Override
    protected Map<String, SkypeConversationInternal> run(WebConnectionBuilder webConnectionBuilder) throws IOException {
        
        JsonObject response = EzSkype.GSON.fromJson(webConnectionBuilder.send(), JsonObject.class);
        
        JsonArray conversations = response.getAsJsonArray("conversations");
    
        Map<String, SkypeConversationInternal> skypeConversations = new HashMap<>();
        
        for (JsonElement jsonElement : conversations) {
            JsonObject conversationJson = jsonElement.getAsJsonObject();
            
            String longId = conversationJson.get("id").getAsString();
    
            String topic;
    
            JsonObject threadProperties = conversationJson.getAsJsonObject("threadProperties");
    
            if (conversationJson.has("threadProperties") && threadProperties.has("topic")) {
                topic = threadProperties.get("topic").getAsString();
            } else {
                topic = "Unknown";
            }
    
            SkypeConversationInternal skypeConversationInternal;
            SkypeConversationType conversationType = SkypeConversationType.fromLongId(longId);
            if (conversationType == SkypeConversationType.USER) {
                skypeConversationInternal = new SkypeUserConversationInternal(ezSkype, longId, "Other " + "user", true, false, "");
            } else {
                skypeConversationInternal = new SkypeGroupConversationInternal(ezSkype, longId, topic, false, new ArrayList<>(), false, 
                        null, null, new ArrayList<>(), new ArrayList<>());
                skypeConversationInternal.setFullyLoaded(false);
            }
    
            skypeConversations.put(skypeConversationInternal.getLongId(), skypeConversationInternal);
        }
    
        return skypeConversations;
    }
}
