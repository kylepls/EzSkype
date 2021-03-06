package in.kyle.ezskypeezlife.internal.packet.conversation;

import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.api.user.SkypeUserRole;
import in.kyle.ezskypeezlife.internal.packet.HTTPRequest;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;

/**
 * Created by Kyle on 10/7/2015.
 */
public class SkypeConversationRolePacket extends SkypePacket {
    
    private final SkypeUserRole skypeUserRole;
    
    public SkypeConversationRolePacket(EzSkype ezSkype, String longId, String username, SkypeUserRole skypeUserRole) {
        super("https://client-s.gateway.messenger.live.com/v1/threads/{}/members/8:{}", HTTPRequest.PUT, ezSkype, true, longId, username);
        this.skypeUserRole = skypeUserRole;
    }
    
    @Override
    protected Object run(WebConnectionBuilder webConnectionBuilder) throws IOException {
        JsonObject data = new JsonObject();
        data.addProperty("Role", StringUtils.capitalize(skypeUserRole.name().toLowerCase()));
        webConnectionBuilder.setPostData(data.toString());
        webConnectionBuilder.send();
        return null;
    }
}
