package in.kyle.ezskypeezlife.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Kyle on 10/7/2015.
 * 
 * Used for better password security
 */
@AllArgsConstructor
public class SkypeCredentials {
    @Getter
    String username;
    @Getter
    private char[] password;
    
    public SkypeCredentials(String username, String password) {
        this(username, password.toCharArray());
    }
}
