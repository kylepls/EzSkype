package in.kyle.ezskypeezlife.internal.packet.auth;

import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.internal.packet.HTTPRequest;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.SkypePacketException;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;

import java.io.IOException;

/**
 * Created by Kyle on 10/20/2015.
 */
public class SkypeAuthEndpointFinalPacket extends SkypePacket {
    
    public SkypeAuthEndpointFinalPacket(EzSkype ezSkype) {
        super("https://{}client-s.gateway.messenger.live.com/v1/users/ME/endpoints/{}/presenceDocs/messagingService", HTTPRequest.PUT, 
                ezSkype, true, ezSkype.getSkypeSession().getLocation(), ezSkype.getSkypeSession().getSessionId());
    }
    
    @Override
    protected Object run(WebConnectionBuilder webConnectionBuilder) throws SkypePacketException, IOException {
        JsonObject data = new JsonObject();
        data.addProperty("id", "messagingService");
        data.addProperty("type", "EndpointPresenceDoc");
        data.addProperty("selfLink", "uri");
        
        JsonObject publicInfo = new JsonObject();
        publicInfo.addProperty("capabilities", "video|audio");
        publicInfo.addProperty("type", 1);
        publicInfo.addProperty("skypeNameVersion", "skype.com");
        publicInfo.addProperty("nodeInfo", 2);
        publicInfo.addProperty("version", 2);
        
        data.add("publicInfo", publicInfo);
        
        JsonObject privateInfo = new JsonObject();
        privateInfo.addProperty("epname", "Skype");
        
        data.add("privateInfo", privateInfo);
        
        webConnectionBuilder.setPostData(data.toString());
        webConnectionBuilder.send();
        return null;
    }
}
