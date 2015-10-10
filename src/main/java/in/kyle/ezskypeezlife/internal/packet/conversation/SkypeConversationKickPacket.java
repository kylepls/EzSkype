package in.kyle.ezskypeezlife.internal.packet.conversation;

import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;

/**
 * Created by Kyle on 10/7/2015.
 */
public class SkypeConversationKickPacket extends SkypePacket {
    
    public SkypeConversationKickPacket(EzSkype ezSkype, String longId, String username) {
        super("https://client-s.gateway.messenger.live.com/v1/threads/" + longId + "/members/8:" + username, WebConnectionBuilder
                .HTTPRequest.DELETE, ezSkype, true);
    }
    
    @Override
    protected Object run(WebConnectionBuilder webConnectionBuilder) throws Exception {
        // TODO do something with this?
        webConnectionBuilder.send();
        return null;
    }
}
