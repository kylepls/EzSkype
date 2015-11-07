package in.kyle.ezskypeezlife.internal.packet.session;

import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.api.SkypeStatus;
import in.kyle.ezskypeezlife.internal.packet.ContentType;
import in.kyle.ezskypeezlife.internal.packet.HTTPRequest;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;
import org.apache.commons.lang.StringUtils;

/**
 * Created by Kyle on 10/8/2015.
 */
public class SkypeSetVisibilityPacket extends SkypePacket {
    
    private final SkypeStatus status;
    
    public SkypeSetVisibilityPacket(EzSkype ezSkype, SkypeStatus status) {
        super("https://client-s.gateway.messenger.live.com/v1/users/ME/presenceDocs/messagingService", HTTPRequest
                .PUT, ezSkype, true);
        this.status = status;
    }
    
    @Override
    protected Object run(WebConnectionBuilder webConnectionBuilder) throws Exception {
        JsonObject data = new JsonObject();
        data.addProperty("status", StringUtils.capitalize(status.name().toLowerCase()));
        webConnectionBuilder.setPostData(data.toString());
        webConnectionBuilder.setContentType(ContentType.JSON);
        webConnectionBuilder.send();
        return null;
    }
}
