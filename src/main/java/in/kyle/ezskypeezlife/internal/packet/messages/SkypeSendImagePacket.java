package in.kyle.ezskypeezlife.internal.packet.messages;

import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;

/**
 * Created by Kyle on 10/7/2015.
 */
public class SkypeSendImagePacket extends SkypePacket {
    
    // TODO
    public SkypeSendImagePacket(String url, WebConnectionBuilder.HTTPRequest httpRequest, EzSkype ezSkype, boolean useHeaders) {
        super(url, httpRequest, ezSkype, useHeaders);
    }
    
    @Override
    protected Object run(WebConnectionBuilder webConnectionBuilder) throws Exception {
        return null;
    }
}
