package in.kyle.ezskypeezlife.internal.guest;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.exception.SkypeException;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.stream.Collectors;

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
    protected String run(WebClient webClient) throws IOException, SkypeGuestGetTokenPacketException {
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
    
        Optional<Cookie> guest_token = webClient.getCookieManager().getCookies().stream().filter(cookie -> cookie.getName().startsWith
                ("guest_token")).findFirst();
    
        if (guest_token.isPresent()) {
            return guest_token.get().getValue();
        } else {
            throw new SkypeGuestGetTokenPacketException("Could not find cookie 'guest_token', if the error persists please try changing "
                    + "your IP\nCookies: [" + StringUtils.join(webClient.getCookieManager().getCookies().stream().map(Cookie::getName)
                    .collect(Collectors.toList()), ",") + "]");
        }
    }
    
    private class SkypeGuestGetTokenPacketException extends SkypeException {
        
        public SkypeGuestGetTokenPacketException(String message) {
            super(message);
        }
    }
}
