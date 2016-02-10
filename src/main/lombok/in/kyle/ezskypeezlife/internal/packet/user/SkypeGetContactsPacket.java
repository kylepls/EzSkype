package in.kyle.ezskypeezlife.internal.packet.user;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.internal.obj.SkypeUserInternal;
import in.kyle.ezskypeezlife.internal.packet.HTTPRequest;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;
import lombok.Data;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Kyle on 10/8/2015.
 */
public class SkypeGetContactsPacket extends SkypePacket {
    
    private static String APP = "?$filter=type%20eq%20%27skype%27%20or%20type%20eq%20%27msn%27%20or%20type%20eq%20%27agent%27&reason" + 
            "=default";
    
    public SkypeGetContactsPacket(EzSkype ezSkype) {
        super("https://contacts.skype.com/contacts/v1/users/{}/contacts{}", HTTPRequest.GET, ezSkype, true, ezSkype.getLocalUser()
                .getUsername(), APP);
    }
    
    @Override
    protected UserContacts run(WebConnectionBuilder webConnectionBuilder) throws IOException {
        JsonObject response = EzSkype.GSON.fromJson(webConnectionBuilder.send(), JsonObject.class);
        JsonArray contactsArray = response.getAsJsonArray("contacts");
        Map<String, SkypeUserInternal> contacts = new HashMap<>();
        Map<String, SkypeUserInternal> pending = new HashMap<>();
        
        for (JsonElement contactElement : contactsArray) {
            JsonObject contactJson = contactElement.getAsJsonObject();
            
            String username = contactJson.get("id").getAsString();
            SkypeUserInternal skypeUser = new SkypeUserInternal(username, ezSkype);
            
            //skypeUser.contact(contactJson.get("authorized").getAsBoolean());
            
            skypeUser.setBlocked(contactJson.get("blocked").getAsBoolean());
            
            if (contactJson.has("avatar_url")) {
                skypeUser.setAvatarUrl(Optional.of(contactJson.get("avatar_url").getAsString()));
            }
            
            skypeUser.setDisplayName(Optional.of(contactJson.get("display_name").getAsString()));
            
            JsonObject nameJson = contactJson.get("name").getAsJsonObject();
            
            if (nameJson.has("first")) {
                skypeUser.setFirstName(Optional.of(nameJson.get("first").getAsString()));
            }
            
            if (nameJson.has("surname")) {
                skypeUser.setLastName(Optional.of(nameJson.get("surname").getAsString()));
            }
            
            if (contactJson.has("locations")) {
                JsonArray locations = contactJson.getAsJsonArray("locations");
                
                if (locations.size() != 0) {
                    JsonObject location = locations.get(0).getAsJsonObject();
                    if (location.has("city")) {
                        skypeUser.setCity(Optional.of(location.get("city").getAsString()));
                    }
                    if (location.has("country")) {
                        skypeUser.setCountry(Optional.of(location.get("country").getAsString()));
                    }
                }
            }
            
            if (contactJson.get("authorized").getAsBoolean()) {
                contacts.put(skypeUser.getUsername(), skypeUser);
            } else {
                pending.put(skypeUser.getUsername(), skypeUser);
            }
        }
        
        return new UserContacts(contacts, pending);
    }
    
    @Data
    public static class UserContacts {
    
        private final Map<String, SkypeUserInternal> contacts;
        private final Map<String, SkypeUserInternal> pending;
    }
}
