package in.kyle.ezskypeezlife.api.obj;

import java.util.Optional;

/**
 * Created by Kyle on 10/10/2015.
 */
public interface SkypeUser {
    
    String getUsername();
    
    Optional<String> getFirstName();
    
    Optional<String> getLastName();
    
    Optional<String> getMood();
    
    Optional<String> getRichMood();
    
    Optional<String> getDisplayName();
    
    Optional<String> getCountry();
    
    Optional<String> getCity();
    
    Optional<String> getAvatarUrl();
    
    boolean isContact();
    
    boolean isBlocked();
    
    void setContact(boolean contact);
    
    SkypeMessage sendMessage(String message);
}
