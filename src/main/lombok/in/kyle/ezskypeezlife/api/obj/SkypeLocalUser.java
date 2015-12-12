package in.kyle.ezskypeezlife.api;

import in.kyle.ezskypeezlife.api.obj.SkypeUser;
import in.kyle.ezskypeezlife.internal.obj.SkypeUserInternal;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Kyle on 11/28/2015.
 */
public interface SkypeLocalUser extends SkypeUser {
    
    Optional<String> getAbout();
    
    Optional<String> getBirthday();
    
    Optional<SkypeGender> getGender();
    
    Optional<String> getLanguage();
    
    Optional<String> getHomepage();
    
    Optional<String> getProvince();
    
    Optional<String> getJobTitle();
    
    List<String> getEmails();
    
    Optional<String> getPhoneMobile();
    
    Optional<String> getPhoneHome();
    
    Optional<String> getPhoneOffice();
    
    Map<String, SkypeUserInternal> getContacts();
    
    Map<String, SkypeUser> getPendingContacts();
}
