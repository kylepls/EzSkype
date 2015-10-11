package in.kyle.ezskypeezlife.internal.packet.session;

import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;

/**
 * Created by Kyle on 10/8/2015.
 */
public class SkypeSessionPingPacket extends SkypePacket {
    
    public SkypeSessionPingPacket(EzSkype ezSkype) {
        super("https://web.skype.com/api/v1/session-ping", WebConnectionBuilder.HTTPRequest.POST, ezSkype, true);
    }
    
    @Override
    protected Object run(WebConnectionBuilder webConnectionBuilder) throws Exception {
        webConnectionBuilder.setContentType(WebConnectionBuilder.ContentType.WWW_FORM);
        String session = ezSkype.getSkypeSession().getSessionId();
        session = session.substring(1, session.length() - 1);
        webConnectionBuilder.setPostData("sessionId=" + session);
        
        webConnectionBuilder.send();
        return null;
    }
}
