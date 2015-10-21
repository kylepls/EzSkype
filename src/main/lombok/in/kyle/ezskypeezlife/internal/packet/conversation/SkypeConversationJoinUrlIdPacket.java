package in.kyle.ezskypeezlife.internal.packet.conversation;

import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;

/**
 * Created by Kyle on 10/20/2015.
 */
public class SkypeConversationJoinUrlIdPacket extends SkypePacket {
    
    public SkypeConversationJoinUrlIdPacket(EzSkype ezSkype, String url) {
        super("https://join.skype.com/api/v1/meetings/" + url.substring(url.lastIndexOf("/") + 1),
                WebConnectionBuilder.HTTPRequest.GET, ezSkype, true);
    }
    
    @Override
    protected String run(WebConnectionBuilder webConnectionBuilder) throws Exception {
        JsonObject response = EzSkype.GSON.fromJson(webConnectionBuilder.send(), JsonObject.class);
        return response.get("longId").getAsString();
    }
}
