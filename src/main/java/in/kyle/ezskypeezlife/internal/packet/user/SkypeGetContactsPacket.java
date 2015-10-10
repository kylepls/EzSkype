package in.kyle.ezskypeezlife.internal.packet.user;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.internal.obj.SkypeUserInternal;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Kyle on 10/8/2015.
 */
public class SkypeGetContactsPacket extends SkypePacket {
    
    public SkypeGetContactsPacket(EzSkype ezSkype) {
        super("tbd", WebConnectionBuilder.HTTPRequest.GET, ezSkype, true);
    }
    
    @Override
    protected List<SkypeUserInternal> run(WebConnectionBuilder webConnectionBuilder) throws Exception {
        String privateUsername = ezSkype.getLocalUser().getUsername();
        
        // TODO ghetto pro
        // Params:
        //  $filter:type eq 'skype' or type eq 'msn' or type eq 'agent'
        //  reason:default
        String append = "?$filter=type%20eq%20%27skype%27%20or%20type%20eq%20%27msn%27%20or%20type%20eq%20%27agent%27&reason=default";
        
        String url = "https://contacts.skype.com/contacts/v1/users/" + privateUsername + "/contacts" + append;
        
        webConnectionBuilder.setUrl(url);
        
        JsonObject response = EzSkype.GSON.fromJson(webConnectionBuilder.send(), JsonObject.class);
        
        JsonArray contactsArray = response.getAsJsonArray("contacts");
        
        // TODO maybe save these to a account obj?
        
        List<SkypeUserInternal> contacts = new ArrayList<>();
        
        for (JsonElement contactElement : contactsArray) {
            JsonObject contactJson = contactElement.getAsJsonObject();
            
            String username = contactJson.get("id").getAsString();
            SkypeUserInternal skypeUser = new SkypeUserInternal(username);
            
            skypeUser.setContact(contactJson.get("authorized").getAsBoolean());
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
                // TODO wtf is this?
                
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
            
            contacts.add(skypeUser);
        }
        
        return contacts;
    }
}