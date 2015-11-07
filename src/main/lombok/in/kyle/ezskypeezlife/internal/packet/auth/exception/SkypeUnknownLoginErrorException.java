package in.kyle.ezskypeezlife.internal.packet.auth.exception;

import java.io.IOException;

/**
 * Created by Kyle on 10/13/2015.
 */
public class SkypeUnknownLoginErrorException extends SkypeLoginException {
    
    public SkypeUnknownLoginErrorException(String html) throws IOException {
        super(html);
    }
}
