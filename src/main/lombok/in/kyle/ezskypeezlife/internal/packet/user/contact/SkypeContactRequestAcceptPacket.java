package in.kyle.ezskypeezlife.internal.packet.user.contact;

import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.internal.packet.HTTPRequest;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;

import java.io.IOException;

/**
 * Created by Kyle on 10/27/2015.
 */
public class SkypeContactRequestAcceptPacket extends SkypePacket {
    
    public SkypeContactRequestAcceptPacket(EzSkype ezSkype, String username) {
        super("https://api.skype.com/users/self/contacts/auth-request/{}/accept", HTTPRequest.PUT, ezSkype, true, username);
    }
    
    @Override
    protected Object run(WebConnectionBuilder webConnectionBuilder) throws IOException {
        webConnectionBuilder.addHeader("X-Stratus-Caller", "skype.com");
        webConnectionBuilder.addHeader("X-Stratus-Request", "a2cf222e");
        webConnectionBuilder.send();
        return null;
    }
}
