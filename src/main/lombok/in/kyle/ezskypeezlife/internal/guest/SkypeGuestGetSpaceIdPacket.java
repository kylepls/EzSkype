package in.kyle.ezskypeezlife.internal.guest;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;

import java.io.IOException;

/**
 * Created by Kyle on 10/23/2015.
 */
public class SkypeGuestGetSpaceIdPacket extends SkypeGuestPacket {
    
    private final String shortId;
    
    public SkypeGuestGetSpaceIdPacket(SkypeWebClient webClient, String shortId) {
        super(webClient);
        this.shortId = shortId;
    }
    
    @Override
    protected Object run(WebClient webClient) throws IOException {
        WebResponse webResponse = webClient.getPage("https://join.skype.com/api/v1/meetings/" + shortId).getWebResponse();
        JsonObject meetingInfo = EzSkype.GSON.fromJson(webResponse.getContentAsString(), JsonObject.class);
        return meetingInfo.get("longId").getAsString();
    }
}
