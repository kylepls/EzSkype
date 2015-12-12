package in.kyle.ezskypeezlife.internal.packet.user;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.internal.obj.SkypeUserInternal;
import in.kyle.ezskypeezlife.internal.packet.HTTPRequest;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Kyle on 10/6/2015.
 * <p>
 * Get information about a user
 * Used to construct SkypeUserInternal object
 */
public class SkypeGetUserInfoPacket extends SkypePacket {
    
    private final List<String> users;
    
    /**
     * @param username - The username of the user you want to get info about
     */
    public SkypeGetUserInfoPacket(EzSkype ezSkype, String username) {
        super("https://api.skype.com/users/self/contacts/profiles", HTTPRequest.POST, ezSkype, true);
        users = Collections.singletonList(username);
    }
    
    @Override
    protected List<SkypeUserInternal> run(WebConnectionBuilder webConnectionBuilder) throws IOException {
    
        for (String user : users) {
            webConnectionBuilder.addPostData("contacts[]", user);
        }
    
        EzSkype.LOGGER.debug("Sending get contact packet, post data: {}", webConnectionBuilder.getPostData().toString());
    
        String data = webConnectionBuilder.send();
    
        JsonArray jsonArray = EzSkype.GSON.fromJson(data, JsonArray.class);
    
        EzSkype.LOGGER.debug("Contact response {}", jsonArray.toString());
    
        if (jsonArray.size() != 0) {
            List<SkypeUserInternal> skypeUsers = new ArrayList<>(users.size());
        
            for (JsonElement jsonElement : jsonArray) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                String username = jsonObject.get("username").getAsString();
                SkypeUserInternal skypeUserInternal = new SkypeUserInternal(username, ezSkype, jsonObject.get("firstname"), jsonObject
                        .get("lastname"), jsonObject.get("avatarUrl"), jsonObject.get("mood"), jsonObject.get("richMood"), jsonObject.get
                        ("displayname"), jsonObject.get("country"), jsonObject.get("city"));
                skypeUsers.add(skypeUserInternal);
            }
        
            return skypeUsers;
        } else {
            throw new IllegalStateException("(This is a temporary error) Bad response: " + data);
        }
    }
}
