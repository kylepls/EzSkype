package in.kyle.ezskypeezlife.internal.obj;

import lombok.AllArgsConstructor;
import lombok.Data;

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
    
}
