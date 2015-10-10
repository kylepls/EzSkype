package in.kyle.ezskypeezlife.internal.packet.conversation;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.api.SkypeConversationPermission;
import in.kyle.ezskypeezlife.api.SkypeUserRole;
import in.kyle.ezskypeezlife.internal.obj.SkypeConversationInternal;
import in.kyle.ezskypeezlife.internal.obj.SkypeGroupConversationInternal;
import in.kyle.ezskypeezlife.internal.obj.SkypeUserInternal;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Kyle on 10/7/2015.
 * <p>
 * Used to create a conversation object from a conversation ID
 */
public class SkypeGetGroupConversationPacket extends SkypePacket {
    
    private final String longId;
    
    public SkypeGetGroupConversationPacket(EzSkype ezSkype, String longId) {
        super("https://client-s.gateway.messenger.live.com/v1/threads/" + longId + "?view=msnp24Equivalent", WebConnectionBuilder
                .HTTPRequest.GET, ezSkype, true);
        this.longId = longId;
    }
    
    @Override
    protected SkypeConversationInternal run(WebConnectionBuilder webConnectionBuilder) throws Exception {
        webConnectionBuilder.setTimeout(5000);
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
            
            creator = ezSkype.getSkypeUser(creatorName);
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
        
        // TODO returned json offers more capabilities for received members & possibly extract some stuffs
        
        List<SkypeUserInternal> members = new ArrayList<>();
        List<SkypeUserInternal> admins = new ArrayList<>();
        
        JsonArray membersJson = convoJson.getAsJsonArray("members");
        
        //System.out.println("Getting conversation: " + longId + " members: " + membersJson.size());
        
        for (JsonElement memberElement : membersJson) {
            JsonObject memberObject = memberElement.getAsJsonObject();
            String username = memberObject.get("id").getAsString();
            username = username.substring(username.indexOf(":") + 1);
            
            SkypeUserInternal skypeUserInternal = ezSkype.getSkypeUser(username);
            
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
