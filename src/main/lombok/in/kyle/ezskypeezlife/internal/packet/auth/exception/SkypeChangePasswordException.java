package in.kyle.ezskypeezlife.internal.packet.auth.exception;

import java.io.IOException;

/**
 * Created by Kyle on 11/6/2015.
 */
public class SkypeChangePasswordException extends SkypeLoginException {
    
    public SkypeChangePasswordException(String html) throws IOException {
        super(html);
    }
}
