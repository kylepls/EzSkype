package in.kyle.ezskypeezlife.internal.thread.poll;

import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.api.user.SkypeUserRole;
import in.kyle.ezskypeezlife.events.conversation.SkypeConversationUserRoleUpdate;
import in.kyle.ezskypeezlife.internal.obj.SkypeGroupConversationInternal;
import in.kyle.ezskypeezlife.internal.obj.SkypeUserInternal;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by Kyle on 10/9/2015.
 */
public class SkypeThreadActivityRoleUpdateType extends SkypePollMessageType {
    
    @Override
    public boolean accept(String messageType) {
        return messageType.equals("ThreadActivity/RoleUpdate");
    }
    
    @Override
    public void extract(EzSkype ezSkype, JsonObject jsonObject, JsonObject resource) throws Exception {
        String longId = getConversationLongId(resource);
        SkypeGroupConversationInternal conversation = (SkypeGroupConversationInternal) ezSkype.getSkypeConversation(longId);
        
        String content = resource.get("content").getAsString();
        Document cont = Jsoup.parse(content);
    
        String user = cont.getElementsByTag("id").text().substring(2);
        SkypeUserInternal skypeUser = (SkypeUserInternal) ezSkype.getSkypeUser(user);
        
        SkypeUserRole oldRole = conversation.getSkypeRole(skypeUser);
        SkypeUserRole role = SkypeUserRole.valueOf(cont.getElementsByTag("role").text().toUpperCase());
        if (role == SkypeUserRole.ADMIN) {
            conversation.getAdmins().add(skypeUser);
        } else {
            conversation.getAdmins().remove(skypeUser);
        }
        SkypeConversationUserRoleUpdate roleUpdate = new SkypeConversationUserRoleUpdate(conversation, skypeUser, oldRole, role);
        ezSkype.getEventManager().fire(roleUpdate);
    }
}
