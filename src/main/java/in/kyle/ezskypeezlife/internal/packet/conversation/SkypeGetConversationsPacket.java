package in.kyle.ezskypeezlife.internal.packet.conversation;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyle on 10/8/2015.
 */
public class SkypeGetConversationsPacket extends SkypePacket {
    
    private static final String URL = "https://client-s.gateway.messenger.live" + "" +
            ".com/v1/users/ME/conversations?startTime=0&pageSize=200&view=msnp24Equivalent&targetType=Passport|Skype|Lync|Thread";
    
    public SkypeGetConversationsPacket(EzSkype ezSkype) {
        super(URL, WebConnectionBuilder.HTTPRequest.GET, ezSkype, true);
    }
    
    @Override
    protected List<String> run(WebConnectionBuilder webConnectionBuilder) throws Exception {
        
        JsonObject response = EzSkype.GSON.fromJson(webConnectionBuilder.send(), JsonObject.class);
        
        JsonArray conversations = response.getAsJsonArray("conversations");
        
        List<String> skypeConversationIds = new ArrayList<>();
        
        for (JsonElement jsonElement : conversations) {
            JsonObject conversationJson = jsonElement.getAsJsonObject();
            
            String longId = conversationJson.get("id").getAsString();
            
            skypeConversationIds.add(longId);
        }
        
        return skypeConversationIds;
    }
}
