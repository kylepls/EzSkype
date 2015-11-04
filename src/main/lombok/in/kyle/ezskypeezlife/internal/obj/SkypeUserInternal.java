package in.kyle.ezskypeezlife.internal.obj;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.api.obj.SkypeConversation;
import in.kyle.ezskypeezlife.api.obj.SkypeMessage;
import in.kyle.ezskypeezlife.api.obj.SkypeUser;
import in.kyle.ezskypeezlife.internal.packet.user.contact.SkypeContactRemovePacket;
import in.kyle.ezskypeezlife.internal.packet.user.contact.SkypeContactRequestAcceptPacket;
import in.kyle.ezskypeezlife.internal.packet.user.contact.SkypeContactRequestSendPacket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Optional;

/**
 * Created by Kyle on 10/6/2015.
 */
@AllArgsConstructor
@Data
@ToString(of = {"username", "contact"})
@EqualsAndHashCode(of = {"username", "contact"})
public class SkypeUserInternal implements SkypeUser {
    
    private String username;
    private EzSkype ezSkype;
    
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
    private boolean loaded;
    
    public SkypeUserInternal(String username, EzSkype ezSkype) {
        this.username = username;
        this.ezSkype = ezSkype;
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
            EzSkype ezSkype,
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
        this(username, ezSkype);
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
        this.loaded = true;
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
    
    public void contact(boolean contact) {
        this.contact = contact;
    }
    
    @Override
    public void setContact(boolean contact) {
        if (contact) {
            //new SkypeAcceptContactRequestMultiPacket(ezSkype, username, "Add me as a contact").executeAsync();
            
            // send request
            // new SkypeSendContactRequestPacket(ezSkype, username, "Add me as a contact").executeAsync();
            //new SkypeAcceptContactRequestMultiPacket(ezSkype, this).executeAsync();
    
            new SkypeContactRequestSendPacket(ezSkype, this).executeAsync();
            new SkypeContactRequestAcceptPacket(ezSkype, username).executeAsync();
    
            //ezSkype.getLocalUser().getContacts().add(this);
        } else {
            try {
                JsonObject o = (JsonObject) new SkypeContactRemovePacket(ezSkype, username).executeSync();
                System.out.println("Response: " + o);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ezSkype.getLocalUser().getContacts().remove(this);
        }
    }
    
    @Override
    public SkypeMessage sendMessage(String message) {
        return getConversation().sendMessage(message);
    }
    
    @Override
    public boolean isFullyLoaded() {
        return loaded;
    }
    
    @Override
    public void fullyLoad() {
        if (!loaded) {
            ezSkype.getSkypeCache().getUsersCache().fullyLoadUser(this);
        }
    }
    
    @Override
    public SkypeConversation getConversation() {
        return ezSkype.getSkypeConversation("8:" + username);
    }
    
    @Override
    public boolean isGuest() {
        return getUsername().startsWith("guest:");
    }
}
