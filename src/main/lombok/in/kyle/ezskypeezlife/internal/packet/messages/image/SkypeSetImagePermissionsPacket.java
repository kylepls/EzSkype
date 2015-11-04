package in.kyle.ezskypeezlife.internal.packet.messages.image;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;

/**
 * Created by Kyle on 11/3/2015.
 */
public class SkypeSetImagePermissionsPacket extends SkypePacket {
    
    private final String longId;
    
    public SkypeSetImagePermissionsPacket(EzSkype ezSkype, String imageId, String longId) {
        super("https://api.asm.skype.com/v1/objects/" + imageId + "/permissions", WebConnectionBuilder.HTTPRequest.PUT, ezSkype, false);
        this.longId = longId;
    }
    
    @Override
    protected Object run(WebConnectionBuilder webConnectionBuilder) throws Exception {
        
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        jsonArray.add("read");
        data.add(longId, jsonArray);
        data.addProperty("type", "pish/image");
        
        webConnectionBuilder.setPostData(data.toString());
        webConnectionBuilder.setContentType(WebConnectionBuilder.ContentType.JSON);
        webConnectionBuilder.addHeader("Authorization", "skype_token " + ezSkype.getSkypeSession().getXToken());
        webConnectionBuilder.send();
        return null;
    }
}
