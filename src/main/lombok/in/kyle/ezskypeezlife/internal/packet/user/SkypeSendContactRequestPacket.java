package in.kyle.ezskypeezlife.internal.packet.user;

import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;

/**
 * Created by Kyle on 10/10/2015.
 */
public class SkypeSendContactRequestPacket extends SkypePacket {
    
    private final String message;
    
    public SkypeSendContactRequestPacket(EzSkype ezSkype, String username, String message) {
        super("https://api.skype.com/users/self/contacts/auth-request/" + username, WebConnectionBuilder.HTTPRequest.PUT, ezSkype, true);
        this.message = message;
    }
    
    @Override
    protected Object run(WebConnectionBuilder webConnectionBuilder) throws Exception {
        webConnectionBuilder.addEncodedPostData("greeting", message);
        webConnectionBuilder.send();
        return null;
    }
}
