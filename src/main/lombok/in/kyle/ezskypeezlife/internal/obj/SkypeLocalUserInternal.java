package in.kyle.ezskypeezlife.internal.obj;

import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.api.SkypeGender;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.*;

/**
 * Created by Kyle on 10/8/2015.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SkypeLocalUserInternal extends SkypeUserInternal {
    
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
    private Set<SkypeUserInternal> contacts;
    
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
        this.contacts = new HashSet<>();
    }
}
