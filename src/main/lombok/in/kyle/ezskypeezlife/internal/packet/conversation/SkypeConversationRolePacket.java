package in.kyle.ezskypeezlife.internal.packet.conversation;

import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.api.SkypeUserRole;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;
import org.apache.commons.lang.StringUtils;

/**
 * Created by Kyle on 10/7/2015.
 */
public class SkypeConversationRolePacket extends SkypePacket {
    
    private final SkypeUserRole skypeUserRole;
    
    public SkypeConversationRolePacket(EzSkype ezSkype, String longId, String username, SkypeUserRole skypeUserRole) {
        super("https://client-s.gateway.messenger.live.com/v1/threads/" + longId + "/members/8:" + username, WebConnectionBuilder
                .HTTPRequest.PUT, ezSkype, true);
        this.skypeUserRole = skypeUserRole;
    }
    
    // TODO
    @Override
    protected Object run(WebConnectionBuilder webConnectionBuilder) throws Exception {
        JsonObject data = new JsonObject();
        data.addProperty("Role", StringUtils.capitalize(skypeUserRole.name().toLowerCase()));
        webConnectionBuilder.setPostData(data.toString());
        webConnectionBuilder.send();
        return null;
    }
}
