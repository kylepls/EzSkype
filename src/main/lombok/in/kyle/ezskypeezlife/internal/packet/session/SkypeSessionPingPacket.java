package in.kyle.ezskypeezlife.internal.packet.session;

import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.internal.packet.ContentType;
import in.kyle.ezskypeezlife.internal.packet.HTTPRequest;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created by Kyle on 10/8/2015.
 */
public class SkypeSessionPingPacket extends SkypePacket {
    
    public SkypeSessionPingPacket(EzSkype ezSkype) {
        super("https://web.skype.com/api/v1/session-ping", HTTPRequest.POST, ezSkype, true);
    }
    
    @Override
    protected Object run(WebConnectionBuilder webConnectionBuilder) throws Exception {
        webConnectionBuilder.setContentType(ContentType.WWW_FORM);
        String session = ezSkype.getSkypeSession().getSessionUuid().toString();
        webConnectionBuilder.setPostData("sessionId=" + session);
        EzSkype.LOGGER.debug("Sending session ping, sessionId={}", session);
        try {
            webConnectionBuilder.send();
        } catch (IOException exception) {
            HttpURLConnection connection = webConnectionBuilder.getConnection();
            if (connection != null) {
                if (connection.getResponseCode() == 403) {
                    throw new SkypeSessionExpiredException();
                }
            }
        }
        return null;
    }
}
