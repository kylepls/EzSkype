package in.kyle.ezskypeezlife.internal.guest;

import com.gargoylesoftware.htmlunit.WebClient;
import in.kyle.ezskypeezlife.exception.SkypeException;

import java.io.IOException;

/**
 * Created by Kyle on 10/23/2015.
 */
public abstract class SkypeGuestPacket {
    
    private final SkypeWebClient webClient;
    
    public SkypeGuestPacket(SkypeWebClient webClient) {
        this.webClient = webClient;
    }
    
    public Object run() throws IOException, SkypeException {
        return run(webClient.getWebClient());
    }
    
    protected abstract Object run(WebClient webClient) throws IOException, SkypeException;
}
