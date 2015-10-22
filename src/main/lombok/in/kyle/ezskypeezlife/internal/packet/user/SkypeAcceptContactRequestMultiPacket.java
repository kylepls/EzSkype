package in.kyle.ezskypeezlife.internal.packet.user;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.api.obj.SkypeUser;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;

/**
 * Created by Kyle on 10/7/2015.
 */
public class SkypeAcceptContactRequestMultiPacket extends SkypePacket {
    
    private final String username;
    
    public SkypeAcceptContactRequestMultiPacket(EzSkype ezSkype, String username) {
        super("https://api.skype.com/users/self/contacts/auth-request/" + username + "/accept", WebConnectionBuilder.HTTPRequest.PUT, 
                ezSkype, true);
        this.username = username;
    }
    
    @Override
    protected JsonObject run(WebConnectionBuilder webConnectionBuilder) throws Exception {
        webConnectionBuilder.setContentType(WebConnectionBuilder.ContentType.WWW_FORM);
        webConnectionBuilder.send();
        
        webConnectionBuilder.setUrl("https://client-s.gateway.messenger.live.com/v1/users/ME/contacts/8:" + username);
        webConnectionBuilder.setRequest(WebConnectionBuilder.HTTPRequest.PUT);
        webConnectionBuilder.send();
    
        JsonObject data = new JsonObject();
        JsonArray contacts = new JsonArray();
        
        for (SkypeUser contactUser : ezSkype.getLocalUser().getContacts()) {
            JsonObject o = new JsonObject();
            o.addProperty("id", contactUser.getUsername());
            contacts.add(o);
        }
    
        JsonObject o = new JsonObject();
        o.addProperty("id", username);
        contacts.add(o);
        
        data.add("contacts", contacts);
        
        webConnectionBuilder.setUrl("https://client-s.gateway.messenger.live.com/v1/users/ME/contacts/");
        webConnectionBuilder.setPostData(data.toString());
        webConnectionBuilder.setContentType(WebConnectionBuilder.ContentType.JSON);
        webConnectionBuilder.setRequest(WebConnectionBuilder.HTTPRequest.POST);
        webConnectionBuilder.send();
        
        return null;
    }
}
