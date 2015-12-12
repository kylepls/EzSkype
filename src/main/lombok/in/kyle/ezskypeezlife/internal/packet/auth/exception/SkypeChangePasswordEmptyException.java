package in.kyle.ezskypeezlife.internal.packet.auth.exception;

import in.kyle.ezskypeezlife.internal.packet.SkypePacketException;
import lombok.Getter;
import lombok.ToString;

import java.io.IOException;

/**
 * Created by Kyle on 11/6/2015.
 */
@ToString(of = "password")
public class SkypeChangePasswordEmptyException extends SkypePacketException {
    
    @Getter
    private final String password;
    
    public SkypeChangePasswordEmptyException(String password) throws IOException {
        this.password = password;
    }
}
