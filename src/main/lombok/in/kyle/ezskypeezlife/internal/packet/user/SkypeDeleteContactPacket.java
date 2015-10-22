package in.kyle.ezskypeezlife.internal.packet.user;

import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;

/**
 * Created by Kyle on 10/7/2015.
 * <p>
 * Removes a user as a contact
 */
public class SkypeDeleteContactPacket extends SkypePacket {
    
    /**
     * @param username - The username of the user you would like to remove
     *                 <p>
     *                 Call getResponse after to get the response
     */
    public SkypeDeleteContactPacket(EzSkype ezSkype, String username) {
        super("https://api.skype.com/users/self/contacts/" + username, WebConnectionBuilder.HTTPRequest.DELETE, ezSkype, true);
    }
    
    @Override
    protected JsonObject run(WebConnectionBuilder webConnectionBuilder) throws Exception {
        return EzSkype.GSON.fromJson(webConnectionBuilder.send(), JsonObject.class);
    }
}
