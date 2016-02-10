package in.kyle.ezskypeezlife.internal.packet.conversation;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.api.user.SkypeUser;
import in.kyle.ezskypeezlife.api.user.SkypeUserRole;
import in.kyle.ezskypeezlife.exception.SkypeException;
import in.kyle.ezskypeezlife.internal.packet.ContentType;
import in.kyle.ezskypeezlife.internal.packet.HTTPRequest;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;

/**
 * Created by Kyle on 12/13/2015.
 */
public class SkypeCreateConversationPacket extends SkypePacket {
    
    private final Map<SkypeUser, SkypeUserRole> users;
    
    public SkypeCreateConversationPacket(EzSkype ezSkype, Map<SkypeUser, SkypeUserRole> users) {
        super("https://client-s.gateway.messenger.live.com/v1/threads", HTTPRequest.POST, ezSkype, true);
        this.users = users;
    }
    
    @Override
    protected Object run(WebConnectionBuilder webConnectionBuilder) throws SkypeCreateConversationException, IOException {
        
        JsonObject data = new JsonObject();
        JsonArray members = new JsonArray();
        
        users.forEach((user, role) -> {
            JsonObject member = new JsonObject();
            member.addProperty("id", "8:" + user.getUsername());
            member.addProperty("role", role.getValue());
            members.add(member);
        });
        
        data.add("members", members);
        
        webConnectionBuilder.setContentType(ContentType.JSON);
        webConnectionBuilder.setPostData(data.toString());
        
        
        JsonObject response = webConnectionBuilder.getAsJsonObject();
        HttpURLConnection connection = webConnectionBuilder.getConnection();
        
        if (connection.getResponseCode() != 201 && connection.getResponseCode() != 207) {
            throw new SkypeCreateConversationException("Response code was not 201, got " + connection.getResponseCode() + "\nResponse: " 
                    + response);
        }
        
        
        if (response.has("multipleStatus")) {
            JsonArray multipleStatus = response.getAsJsonArray("multipleStatus");
            for (JsonElement jsonElement : multipleStatus) {
                JsonObject memberResponse = jsonElement.getAsJsonObject();
                String id = memberResponse.get("id").getAsString();
                int code = memberResponse.get("statusCode").getAsInt();
                if (id.equals("/threads")) {
                    if (code != 0) {
                        throw new SkypeCreateConversationException("Could not create conversation, Skype returned error code " + code +
                                "\n Json: " + response);
                    }
                } else if (code != 0) {
                    logger.debug("Error adding user {} to new conversation, Skype returned error code {}. If this code is 206 " +
                            "please make sure to add them as a contact before creating a new conversation. Json: {}", id, code, response);
                }
            }
        }
        
        String longId = webConnectionBuilder.getConnection().getHeaderField("Location");
        longId = longId.substring(longId.lastIndexOf("/") + 1);
        
        return ezSkype.getSkypeConversation(longId);
    }
    
    private class SkypeCreateConversationException extends SkypeException {
        
        public SkypeCreateConversationException(String message) {
            super(message);
        }
    }
}
