package in.kyle.ezskypeezlife.internal.packet.user;

import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.api.user.SkypeUserProperties;
import in.kyle.ezskypeezlife.internal.packet.HTTPRequest;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;

import java.io.IOException;

/**
 * Created by Kyle on 11/3/2015.
 */
public class SkypeUserPropertiesPacket extends SkypePacket {
    
    public SkypeUserPropertiesPacket(EzSkype ezSkype) {
        super("https://client-s.gateway.messenger.live.com/v1/users/ME/properties", HTTPRequest.OPTIONS, ezSkype, true);
    }
    
    @Override
    protected SkypeUserProperties run(WebConnectionBuilder webConnectionBuilder) throws IOException {
        JsonObject jsonObject = EzSkype.GSON.fromJson(webConnectionBuilder.send(), JsonObject.class);
    
        return new SkypeUserProperties(jsonObject.get("cid").getAsInt(), jsonObject.get("dogfoodUser").getAsBoolean(), jsonObject.get
                ("primaryMemberName").getAsString(), jsonObject.get("skypeName").getAsString());
    }
}
