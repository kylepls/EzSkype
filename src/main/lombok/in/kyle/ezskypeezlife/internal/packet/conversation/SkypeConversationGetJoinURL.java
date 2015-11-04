package in.kyle.ezskypeezlife.internal.packet.conversation;

import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;

/**
 * Created by Kyle on 10/20/2015.
 */
public class SkypeConversationGetJoinUrl extends SkypePacket {
    
    private final String longId;
    
    public SkypeConversationGetJoinUrl(EzSkype ezSkype, String longId) {
        super("https://api.scheduler.skype.com/threads", WebConnectionBuilder.HTTPRequest.POST, ezSkype, true);
        this.longId = longId;
    }
    
    @Override
    protected Object run(WebConnectionBuilder webConnectionBuilder) throws Exception {
        JsonObject data = new JsonObject();
        data.addProperty("baseDomain", "https://join.skype.com/launch/");
        data.addProperty("threadId", longId);
        
        webConnectionBuilder.setPostData(data.toString());
        
        JsonObject response = EzSkype.GSON.fromJson(webConnectionBuilder.send(), JsonObject.class);
        
        return response.get("JoinUrl").getAsString();
    }
}
