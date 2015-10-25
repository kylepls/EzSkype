package in.kyle.ezskypeezlife.internal.guest;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;

/**
 * Created by Kyle on 10/23/2015.
 */
public class SkypeGuestGetConversationId extends SkypeGuestPacket {
    
    private final String spaceId;
    
    public SkypeGuestGetConversationId(SkypeWebClient webClient, String spaceId) {
        super(webClient);
        this.spaceId = spaceId;
    }
    
    @Override
    protected Object run(WebClient webClient) throws Exception {
        WebResponse webResponse = webClient.getPage("https://api.scheduler.skype.com/conversation/" + spaceId).getWebResponse();
        JsonObject moreInfo = EzSkype.GSON.fromJson(webResponse.getContentAsString(), JsonObject.class);
        return moreInfo.get("ThreadId").getAsString();
    }
}
