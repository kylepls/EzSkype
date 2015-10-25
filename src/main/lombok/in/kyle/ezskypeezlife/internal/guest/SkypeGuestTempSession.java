package in.kyle.ezskypeezlife.internal.guest;

import lombok.Data;

/**
 * Created by Kyle on 10/23/2015.
 */
@Data
public class SkypeGuestTempSession {
    
    private final String csrf;
    private final String sessionId;
    
}
