package in.kyle.ezskypeezlife.internal.packet.user;

import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.internal.packet.ContentType;
import in.kyle.ezskypeezlife.internal.packet.HTTPRequest;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;

import java.io.IOException;

/**
 * Created by Kyle on 10/7/2015.
 * <p>
 * Sends a contact request to a user
 */
public class SkypeSendContactRequestPacket extends SkypePacket {
    
    private String username;
    private String greeting;
    
    public SkypeSendContactRequestPacket(EzSkype ezSkype, String username) {
        this(ezSkype, username, "Hi, I'd like to add you as a contact.");
    }
    
    /**
     * @param username - The username to send the request too
     * @param greeting - The message/greeting sent with the contact request
     *                 <p>
     *                 Use getResponse after to get the response from the server
     */
    public SkypeSendContactRequestPacket(EzSkype ezSkype, String username, String greeting) {
        super("https://api.skype.com/users/self/contacts/auth-request/" + username, HTTPRequest.PUT, ezSkype, true);
        this.username = username;
        this.greeting = greeting;
    }
    
    @Override
    protected JsonObject run(WebConnectionBuilder webConnectionBuilder) throws IOException {
        webConnectionBuilder.setContentType(ContentType.WWW_FORM);
        // TODO something looks wrong here
        return webConnectionBuilder.getAsJsonObject();
    }
}
