package in.kyle.ezskypeezlife.internal.packet.session;

import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.exception.SkypeException;
import in.kyle.ezskypeezlife.internal.packet.ContentType;
import in.kyle.ezskypeezlife.internal.packet.HTTPRequest;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Kyle on 10/8/2015.
 */
public class SkypeActivePacket extends SkypePacket {
    
    public SkypeActivePacket(EzSkype ezSkype) throws UnsupportedEncodingException {
        super("https://client-s.gateway.messenger.live.com/v1/users/ME/endpoints/{}/active", HTTPRequest.POST, ezSkype, true, URLEncoder
                .encode(ezSkype.getSkypeSession().getSessionId(), "UTF-8"));
    }
    
    @Override
    protected Object run(WebConnectionBuilder webConnectionBuilder) throws IOException, SkypeException {
        JsonObject data = new JsonObject();
        data.addProperty("timeout", 12);
    
        webConnectionBuilder.setContentType(ContentType.JSON);
        webConnectionBuilder.setPostData(data.toString());
    
        try {
            webConnectionBuilder.send();
        } catch (FileNotFoundException e) {
            throw new SkypeSessionExpiredException();
        }
        return null;
    }
}
