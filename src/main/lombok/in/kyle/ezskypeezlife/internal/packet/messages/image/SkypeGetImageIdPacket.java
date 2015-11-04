package in.kyle.ezskypeezlife.internal.packet.messages.image;

import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;

/**
 * Created by Kyle on 11/3/2015.
 */
public class SkypeGetImageIdPacket extends SkypePacket {
    
    public SkypeGetImageIdPacket(EzSkype ezSkype) {
        super("https://api.asm.skype.com/v1/objects", WebConnectionBuilder.HTTPRequest.POST, ezSkype, false);
    }
    
    @Override
    protected String run(WebConnectionBuilder webConnectionBuilder) throws Exception {
        webConnectionBuilder.addHeader("Authorization", "skype_token " + ezSkype.getSkypeSession().getXToken());
        String data = webConnectionBuilder.send();
        return EzSkype.GSON.fromJson(data, JsonObject.class).get("id").getAsString();
    }
}
