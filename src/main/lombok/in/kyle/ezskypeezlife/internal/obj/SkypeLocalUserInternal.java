package in.kyle.ezskypeezlife.internal.obj;

import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.api.SkypeGender;
import in.kyle.ezskypeezlife.api.SkypeLocalUser;
import in.kyle.ezskypeezlife.api.obj.SkypeUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Kyle on 10/8/2015.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SkypeLocalUserInternal extends SkypeUserInternal implements SkypeLocalUser {
    
    private String username;
    private Optional<String> about;
    private Optional<String> birthday;
    private Optional<SkypeGender> gender;
    private Optional<String> language;
    private Optional<String> homepage;
    private Optional<String> province;
    private Optional<String> jobTitle;
    private List<String> emails;
    private Optional<String> phoneMobile;
    private Optional<String> phoneHome;
    private Optional<String> phoneOffice;
    private Map<String, SkypeUserInternal> contacts;
    private Map<String, SkypeUserInternal> pendingContacts;
    
    public SkypeLocalUserInternal(String username, EzSkype ezSkype) {
        super(username, ezSkype);
        this.username = username;
        this.about = Optional.empty();
        this.birthday = Optional.empty();
        this.gender = Optional.empty();
        this.language = Optional.empty();
        this.homepage = Optional.empty();
        this.province = Optional.empty();
        this.jobTitle = Optional.empty();
        this.emails = new ArrayList<>();
        this.phoneMobile = Optional.empty();
        this.phoneHome = Optional.empty();
        this.phoneOffice = Optional.empty();
        this.contacts = new HashMap<>();
        this.pendingContacts = new HashMap<>();
    }
    
    /**
     * Gets a list of people waiting for contact acceptance
     *
     * @return - A list of pending contacts
     */
    public Map<String, SkypeUser> getPendingContacts() {
        return (Map<String, SkypeUser>) (Object) pendingContacts;
    }
}
