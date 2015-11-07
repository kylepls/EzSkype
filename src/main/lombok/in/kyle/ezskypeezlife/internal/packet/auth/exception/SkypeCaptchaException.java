package in.kyle.ezskypeezlife.internal.packet.auth.exception;

import java.io.IOException;

/**
 * Created by Kyle on 11/6/2015.
 */
public class SkypeCaptchaException extends SkypeLoginException {
    
    public SkypeCaptchaException(String html) throws IOException {
        super(html);
    }
}
