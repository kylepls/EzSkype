package in.kyle.ezskypeezlife.internal.packet.user.contact;

import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.internal.packet.HTTPRequest;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;

import java.io.IOException;

/**
 * Created by Kyle on 10/7/2015.
 * <p>
 * Removes a user as a contact
 */
public class SkypeContactRemovePacket extends SkypePacket {
    
    private final String username;
    
    /**
     * @param username - The username of the user you would like to remove
     *                 <p>
     *                 Call getResponse after to get the response
     */
    public SkypeContactRemovePacket(EzSkype ezSkype, String username) {
        super("https://api.skype.com/users/self/contacts/" + username, HTTPRequest.DELETE, ezSkype, true);
        this.username = username;
    }
    
    @Override
    protected JsonObject run(WebConnectionBuilder webConnectionBuilder) throws IOException {
        //String id = (String) new SkypeContactEditPacket(ezSkype, username).executeSync();
        webConnectionBuilder.addHeader("X-Stratus-Caller", "skype.com");
        webConnectionBuilder.addHeader("X-Stratus-Request", "a2cf222e");
        return EzSkype.GSON.fromJson(webConnectionBuilder.send(), JsonObject.class);
    }
}
