package in.kyle.ezskypeezlife.internal.packet.conversation;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.api.conversation.SkypeConversationPermission;
import in.kyle.ezskypeezlife.api.user.SkypeUserRole;
import in.kyle.ezskypeezlife.internal.obj.SkypeGroupConversationInternal;
import in.kyle.ezskypeezlife.internal.obj.SkypeUserInternal;
import in.kyle.ezskypeezlife.internal.packet.HTTPRequest;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Kyle on 10/7/2015.
 * <p>
 * Used to create a conversation object from a conversation ID
 */
public class SkypeGetGroupConversationPacket extends SkypePacket {
    
    public SkypeGetGroupConversationPacket(EzSkype ezSkype, String longId) {
        super("https://client-s.gateway.messenger.live.com/v1/threads/" + longId + "?view=msnp24Equivalent", HTTPRequest.GET, ezSkype, 
                true);
    }
    
    @Override
    protected SkypeGroupConversationInternal run(WebConnectionBuilder webConnectionBuilder) throws IOException {
        JsonObject convoJson = EzSkype.GSON.fromJson(webConnectionBuilder.send(), JsonObject.class);
        
        JsonObject propertiesJson = convoJson.getAsJsonObject("properties");
        
        /*
        String picture = propertiesJson.get("picture").getAsString();
        if (!picture.isEmpty()) {
            picture = picture.substring(4);
        }
         */
        
        SkypeUserInternal creator = null;
        if (propertiesJson.has("creator")) {
            String creatorName = propertiesJson.get("creator").getAsString();
            creatorName = creatorName.substring(creatorName.indexOf(":") + 1);
            
            creator = (SkypeUserInternal) ezSkype.getSkypeUser(creatorName);
        }
        
        boolean historyEnabled = true;
        if (propertiesJson.has("historydisclosed")) {
            historyEnabled = propertiesJson.get("historydisclosed").getAsBoolean();
        }
        
        boolean joinEnabled = true;
        if (propertiesJson.has("joiningenabled")) {
            joinEnabled = propertiesJson.get("joiningenabled").getAsBoolean();
        }
        List<SkypeConversationPermission> permissions = new ArrayList<>();
        
        for (JsonElement jsonElement : propertiesJson.getAsJsonArray("capabilities")) {
            permissions.add(SkypeConversationPermission.getFromSkypeString(jsonElement.getAsString()));
        }
        
        List<SkypeUserInternal> members = new ArrayList<>();
        List<SkypeUserInternal> admins = new ArrayList<>();
        
        JsonArray membersJson = convoJson.getAsJsonArray("members");
        
        //System.out.println("Getting conversation: " + longId + " members: " + membersJson.size());
        
        for (JsonElement memberElement : membersJson) {
            JsonObject memberObject = memberElement.getAsJsonObject();
            String username = memberObject.get("id").getAsString();
            username = username.substring(username.indexOf(":") + 1);
            
            SkypeUserInternal skypeUserInternal = ezSkype.getSkypeCache().getUsersCache().getOrCreateUserUnloaded
                    (username);
            
            SkypeUserRole role = SkypeUserRole.valueOf(memberObject.get("role").getAsString().toUpperCase());
            
            if (role == SkypeUserRole.ADMIN) {
                admins.add(skypeUserInternal);
            }
            members.add(skypeUserInternal);
        }
        
        String messagesUrl = convoJson.get("messages").getAsString();
        
        String longId = messagesUrl.substring(messagesUrl.indexOf("conversations/") + 14, messagesUrl.lastIndexOf("/"));
        
        String topic;
        if (propertiesJson.has("topic")) {
            topic = propertiesJson.get("topic").getAsString();
        } else {
            topic = StringUtils.join(members.stream().map(SkypeUserInternal::getUsername).collect(Collectors.toList()), ' ');
        }
        
        return new SkypeGroupConversationInternal(ezSkype, longId, topic, historyEnabled, permissions, joinEnabled, creator, messagesUrl,
                members, admins);
    }
}
