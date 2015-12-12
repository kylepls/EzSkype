package in.kyle.ezskypeezlife.internal.packet.user.contact;

import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.internal.packet.HTTPRequest;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;

import java.io.IOException;

/**
 * Created by Kyle on 10/27/2015.
 */
public class SkypeContactEditPacket extends SkypePacket {
    
    public SkypeContactEditPacket(EzSkype ezSkype, String username) {
        super("https://client-s.gateway.messenger.live.com/v1/users/ME/contacts/8:" + username, HTTPRequest.OPTIONS,
                ezSkype, true);
    }
    
    @Override
    protected Object run(WebConnectionBuilder webConnectionBuilder) throws IOException {
        webConnectionBuilder.send();
        return webConnectionBuilder.getConnection().getHeaderFields().get("ContextId");
    }
}
