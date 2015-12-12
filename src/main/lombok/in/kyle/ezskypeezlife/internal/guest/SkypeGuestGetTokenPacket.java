package in.kyle.ezskypeezlife.internal.guest;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Kyle on 10/23/2015.
 */
public class SkypeGuestGetTokenPacket extends SkypeGuestPacket {
    
    private final String username;
    private final String spaceId;
    private final String threadId;
    private final SkypeGuestTempSession tempSession;
    private final String shortId;
    
    public SkypeGuestGetTokenPacket(SkypeWebClient webClient, SkypeGuestTempSession tempSession, String username, String spaceId, String 
            threadId, String shortId) {
        super(webClient);
        this.tempSession = tempSession;
        this.username = username;
        this.spaceId = spaceId;
        this.threadId = threadId;
        this.shortId = shortId;
    }
    
    @Override
    protected String run(WebClient webClient) throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("flowId", tempSession.getSessionId());
        jsonObject.addProperty("name", username);
        jsonObject.addProperty("spaceId", spaceId);
        jsonObject.addProperty("threadId", threadId);
        
        WebRequest webRequest = new WebRequest(new URL("https://join.skype.com/api/v1/users/guests?v=" + System.currentTimeMillis()), 
                HttpMethod.POST);
        webRequest.setAdditionalHeader("X-Skype-Request-Id", tempSession.getSessionId());
        webRequest.setAdditionalHeader("csrf_token", tempSession.getCsrf());
        webRequest.setAdditionalHeader("Content-Type", "application/json");
        webRequest.setAdditionalHeader("Accept", "application/json");
        webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
        webRequest.setAdditionalHeader("Accept-Language", "en-US,en;q=0.8");
        webRequest.setAdditionalHeader("Accept", "*/*");
        webRequest.setAdditionalHeader("Host", "join.skype.com");
        webRequest.setAdditionalHeader("Origin", "https://join.skype.com");
        webRequest.setAdditionalHeader("Referer", "https://join.skype.com/" + shortId);
        webRequest.setRequestBody(jsonObject.toString());
        
        webClient.getPage(webRequest).getWebResponse();
        
        return webClient.getCookieManager().getCookies().stream().filter(cookie -> cookie.getName().startsWith("guest_token")).findFirst
                ().orElse(null).getValue();
    }
}
