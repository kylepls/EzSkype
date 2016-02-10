package in.kyle.ezskypeezlife.internal.packet.conversation;

import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.internal.packet.HTTPRequest;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;

import java.io.IOException;

/**
 * Created by Kyle on 10/7/2015.
 */
public class SkypeConversationKickPacket extends SkypePacket {
    
    public SkypeConversationKickPacket(EzSkype ezSkype, String longId, String username) {
        super("https://client-s.gateway.messenger.live.com/v1/threads/{}/members/8:{}", HTTPRequest.DELETE, ezSkype, true, longId, 
                username);
    }
    
    @Override
    protected Object run(WebConnectionBuilder webConnectionBuilder) throws IOException {
        webConnectionBuilder.send();
        return null;
    }
}
