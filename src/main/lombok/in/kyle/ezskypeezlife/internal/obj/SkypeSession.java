package in.kyle.ezskypeezlife.internal.obj;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Kyle on 10/6/2015.
 */
@Data
@AllArgsConstructor
public class SkypeSession {
    
    private String regToken;
    private String xToken;
    private String location;
    private String sessionId;
    
    public String getEncodedSessionId() {
        try {
            return URLEncoder.encode(sessionId, "UTF-8");
        } catch (UnsupportedEncodingException ignored) {
            throw new UnsupportedOperationException("Cannot convert string to UTF-8");
        }
    }
}
