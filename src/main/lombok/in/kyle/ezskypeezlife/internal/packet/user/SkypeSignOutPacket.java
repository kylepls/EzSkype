package in.kyle.ezskypeezlife.internal.packet.user;

import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.internal.packet.HTTPRequest;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;

import java.io.IOException;

/**
 * Created by Kyle on 12/12/2015.
 */
public class SkypeSignOutPacket extends SkypePacket {
    
    public SkypeSignOutPacket(EzSkype ezSkype) {
        super("https://login.skype.com/logout?client_id=578134&redirect_uri=https%3A%2F%2Fweb.skype.com", HTTPRequest.GET, ezSkype, true);
    }
    
    @Override
    protected Object run(WebConnectionBuilder webConnectionBuilder) throws IOException {
        webConnectionBuilder.send();
        return null;
    }
}
