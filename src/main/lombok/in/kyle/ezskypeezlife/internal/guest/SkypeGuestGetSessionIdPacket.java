package in.kyle.ezskypeezlife.internal.guest;

import com.gargoylesoftware.htmlunit.WebClient;

/**
 * Created by Kyle on 10/23/2015.
 */
public class SkypeGuestGetSessionIdPacket extends SkypeGuestPacket {
    
    private final String joinUrl;
    
    public SkypeGuestGetSessionIdPacket(SkypeWebClient webClient, String joinUrl) {
        super(webClient);
        this.joinUrl = joinUrl;
    }
    
    @Override
    protected SkypeGuestTempSession run(WebClient webClient) throws Exception {
        webClient.getPage(joinUrl);
        String csrf = webClient.getCookieManager().getCookie("csrf_token").getValue();
        String sessionId = webClient.getCookieManager().getCookie("launcher_session_id").getValue();
        return new SkypeGuestTempSession(csrf, sessionId);
    }
}
