package in.kyle.ezskypeezlife.api.captcha;

import lombok.Data;

/**
 * Created by Kyle on 11/5/2015.
 */
@Data
public class SkypeCaptcha {
    
    private final String token;
    private final String fid;
    private final String url;
    private String solution;
    
}
