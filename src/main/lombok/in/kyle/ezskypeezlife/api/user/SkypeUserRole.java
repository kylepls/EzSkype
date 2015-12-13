package in.kyle.ezskypeezlife.api.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Kyle on 10/7/2015.
 * <p>
 * Role of a user in a conversation
 */
@AllArgsConstructor
public enum SkypeUserRole {
    USER("User"),
    ADMIN("Admin");
    
    @Getter
    private final String value;
}
