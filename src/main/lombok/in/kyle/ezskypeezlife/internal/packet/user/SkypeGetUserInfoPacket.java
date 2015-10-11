package in.kyle.ezskypeezlife.internal.packet.user;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.internal.obj.SkypeUserInternal;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;

/**
 * Created by Kyle on 10/6/2015.
 * <p>
 * Get information about a user
 * Used to construct SkypeUserInternal object
 */
public class SkypeGetUserInfoPacket extends SkypePacket {
    
    private String username;
    
    /**
     * @param username - The username of the user you want to get info about
     */
    public SkypeGetUserInfoPacket(EzSkype ezSkype, String username) {
        super("https://api.skype.com/users/self/contacts/profiles", WebConnectionBuilder.HTTPRequest.POST, ezSkype, true);
        this.username = username;
    }
    
    @Override
    protected SkypeUserInternal run(WebConnectionBuilder webConnectionBuilder) throws Exception {
        webConnectionBuilder.setPostData("contacts[]=" + username);
        webConnectionBuilder.setContentType(WebConnectionBuilder.ContentType.WWW_FORM);
        
        String data = webConnectionBuilder.send();
        
        
        JsonArray jsonArray = EzSkype.GSON.fromJson(data, JsonArray.class);
        
        JsonObject user = jsonArray.get(0).getAsJsonObject();
        
        SkypeUserInternal skypeUserInternal = new SkypeUserInternal(username, ezSkype, user.get("firstname"), user.get("lastname"), user.get
                ("avatarUrl"), user.get("mood"), user.get("richMood"), user.get("displayname"), user.get("country"), user.get("city"));
        
        return skypeUserInternal;
    }
}
