package in.kyle.ezskypeezlife.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Kyle on 10/7/2015.
 * 
 * Used for better password security
 */
@AllArgsConstructor
public class SkypeCredentials {
    @Getter
    private String username;
    @Getter
    @Setter
    private char[] password;
    
    public SkypeCredentials(String username, String password) {
        this(username, password.toCharArray());
    }
    
    /**
     * This constructor is used for guest accounts only
     *
     * @param username - The username
     */
    public SkypeCredentials(String username) {
        this(username, new char[0]);
    }
    
    public boolean isGuestAccount() {
        return password.length == 0;
    }
}
