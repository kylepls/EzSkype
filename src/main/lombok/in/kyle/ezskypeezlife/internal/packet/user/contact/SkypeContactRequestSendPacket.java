package in.kyle.ezskypeezlife.internal.packet.user.contact;

import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.api.obj.SkypeUser;
import in.kyle.ezskypeezlife.internal.packet.HTTPRequest;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;

import java.io.IOException;

/**
 * Created by Kyle on 10/27/2015.
 */
public class SkypeContactRequestSendPacket extends SkypePacket {
    
    private final String greeting;
    
    public SkypeContactRequestSendPacket(EzSkype ezSkype, SkypeUser user) {
        this(ezSkype, user.getUsername(), "Hi, " + (user.getDisplayName().isPresent() ? user.getDisplayName().get() : user.getUsername())
                + ", I'd like to add you as a contact.");
    }
    
    public SkypeContactRequestSendPacket(EzSkype ezSkype, String username, String greeting) {
        super("https://api.skype.com/users/self/contacts/auth-request/" + username, HTTPRequest.PUT, ezSkype, true);
        this.greeting = greeting;
    }
    
    @Override
    protected Object run(WebConnectionBuilder webConnectionBuilder) throws IOException {
        JsonObject jsonObject = new JsonObject();
        webConnectionBuilder.setPostData(jsonObject.toString());
        webConnectionBuilder.addPostData("greeting", greeting);
        webConnectionBuilder.addHeader("X-Stratus-Caller", "skype.com");
        webConnectionBuilder.addHeader("X-Stratus-Request", "a2cf222e");
        webConnectionBuilder.send();
        
        return null;
    }
}
