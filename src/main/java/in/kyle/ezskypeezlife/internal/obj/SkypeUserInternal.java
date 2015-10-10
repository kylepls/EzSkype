package in.kyle.ezskypeezlife.internal.obj;

import com.google.gson.JsonElement;
import in.kyle.ezskypeezlife.api.obj.SkypeUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Optional;

/**
 * Created by Kyle on 10/6/2015.
 */
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = "username")
public class SkypeUserInternal implements SkypeUser {
    
    private String username;
    
    private Optional<String> firstName;
    private Optional<String> lastName;
    private Optional<String> mood;
    private Optional<String> richMood;
    private Optional<String> displayName;
    private Optional<String> country;
    private Optional<String> city;
    private Optional<String> avatarUrl;
    private boolean contact;
    private boolean blocked;
    
    public SkypeUserInternal(String username) {
        this.username = username;
        this.firstName = Optional.empty();
        this.lastName = Optional.empty();
        this.mood = Optional.empty();
        this.richMood = Optional.empty();
        this.displayName = Optional.empty();
        this.country = Optional.empty();
        this.city = Optional.empty();
        this.avatarUrl = Optional.empty();
    }
    
    public SkypeUserInternal(
            // @formatter:off
            String username,
            JsonElement firstName,
            JsonElement lastName,
            JsonElement mood,
            JsonElement richMood, 
            JsonElement displayName,
            JsonElement country,
            JsonElement city,
            JsonElement avatarUrl
            // @formatter:on
    ) {
        this(username);
        if (!firstName.isJsonNull()) {
            this.firstName = Optional.of(firstName.getAsString());
        }
        if (!lastName.isJsonNull()) {
            this.lastName = Optional.of(lastName.getAsString());
        }
        if (!mood.isJsonNull()) {
            this.mood = Optional.of(mood.getAsString());
        }
        if (!richMood.isJsonNull()) {
            this.richMood = Optional.of(richMood.getAsString());
        }
        if (!displayName.isJsonNull()) {
            this.displayName = Optional.of(displayName.getAsString());
        }
        if (!country.isJsonNull()) {
            this.country = Optional.of(country.getAsString());
        }
        if (!city.isJsonNull()) {
            this.city = Optional.of(city.getAsString());
        }
        if (!avatarUrl.isJsonNull()) {
            this.avatarUrl = Optional.of(avatarUrl.getAsString());
        }
    }
    
    /**
     * @return Display name if present and the username as a backup
     */
    public String getDisplayNameOrUsername() {
        if (displayName.isPresent()) {
            return displayName.get();
        } else {
            return username;
        }
    }
    
    // TODO work on avatar urls
    //return "https://api.skype.com/users/" + username + "/profile/avatar";
    
    // TODO send message & whatnot
    
}
