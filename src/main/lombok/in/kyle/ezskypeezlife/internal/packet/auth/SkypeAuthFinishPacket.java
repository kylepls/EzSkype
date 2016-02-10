package in.kyle.ezskypeezlife.internal.packet.auth;

import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.internal.obj.SkypeSession;
import in.kyle.ezskypeezlife.internal.packet.HTTPRequest;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Kyle on 10/5/2015.
 */
public class SkypeAuthFinishPacket extends SkypePacket {
    
    private final String token;
    
    public SkypeAuthFinishPacket(EzSkype ezSkype, String token) {
        super("https://client-s.gateway.messenger.live.com/v1/users/ME/endpoints", HTTPRequest.POST, ezSkype, false);
        this.token = token;
    }
    
    @Override
    protected SkypeSession run(WebConnectionBuilder webConnectionBuilder) throws IOException {
        webConnectionBuilder.addHeader("Authentication", "skypetoken=" + token);
        webConnectionBuilder.setPostData("{}");
        webConnectionBuilder.send();
        
        String location = webConnectionBuilder.getConnection().getHeaderField("Location");
        location = location.substring(location.indexOf("//") + 2, location.indexOf("client"));
        
        String[] tokenOre = webConnectionBuilder.getConnection().getHeaderField("Set-RegistrationToken").split(";");
        String regToken = tokenOre[0];
        
        String endpoint = tokenOre[2].substring(tokenOre[2].indexOf("=") + 1);
        UUID sessionUuid = UUID.randomUUID();
    
        logger.debug("   Session data:");
        logger.debug("      RegToken: {}", regToken);
        logger.debug("      XToken: {}", token);
        logger.debug("      Location: {}", location);
        logger.debug("      Session Id: {}", endpoint);
        logger.debug("      Session UUID: {}", sessionUuid);
    
        return new SkypeSession(regToken, token, location, endpoint, sessionUuid);
    }
}
