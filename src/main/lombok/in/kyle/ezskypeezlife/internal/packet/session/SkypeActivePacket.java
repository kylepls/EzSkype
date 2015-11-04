package in.kyle.ezskypeezlife.internal.packet.session;

import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;

import java.io.FileNotFoundException;
import java.net.URLEncoder;

/**
 * Created by Kyle on 10/8/2015.
 */
public class SkypeActivePacket extends SkypePacket {
    
    public SkypeActivePacket(EzSkype ezSkype) {
        super("tbd", WebConnectionBuilder.HTTPRequest.POST, ezSkype, true);
    }
    
    @Override
    protected Object run(WebConnectionBuilder webConnectionBuilder) throws Exception {
        String url = "https://client-s.gateway.messenger.live.com/v1/users/ME/endpoints/" + URLEncoder.encode(ezSkype.getSkypeSession()
                .getSessionId(), "UTF-8") + "/active";
        webConnectionBuilder.setUrl(url);
        
        JsonObject data = new JsonObject();
        data.addProperty("timeout", 12);
        
        webConnectionBuilder.setContentType(WebConnectionBuilder.ContentType.JSON);
        webConnectionBuilder.setPostData(data.toString());
    
        EzSkype.LOGGER.debug("Sending skype active packet, url: {}", url);
    
        try {
            webConnectionBuilder.send();
        } catch (FileNotFoundException e) {
            throw new SkypeSessionExpiredException();
        }
        return null;
    }
}
