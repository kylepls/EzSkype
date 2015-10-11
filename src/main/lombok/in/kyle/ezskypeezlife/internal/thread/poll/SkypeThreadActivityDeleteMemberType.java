package in.kyle.ezskypeezlife.internal.thread.poll;

import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.events.conversation.SkypeConversationKickedFromEvent;
import in.kyle.ezskypeezlife.events.conversation.SkypeConversationUserLeaveEvent;
import in.kyle.ezskypeezlife.internal.obj.SkypeGroupConversationInternal;
import in.kyle.ezskypeezlife.internal.obj.SkypeUserInternal;
import in.kyle.ezskypeezlife.internal.thread.SkypePollMessageType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by Kyle on 10/9/2015.
 */
public class SkypeThreadActivityDeleteMemberType extends SkypePollMessageType {
    
    @Override
    public boolean accept(String messageType) {
        return messageType.equals("ThreadActivity/DeleteMember");
    }
    
    @Override
    public void extract(EzSkype ezSkype, JsonObject jsonObject, JsonObject resource) throws Exception {
        String longId = getConversationLongId(resource);
        SkypeGroupConversationInternal conversation = (SkypeGroupConversationInternal) ezSkype.getSkypeConversation(longId);
        
        String content = resource.get("content").getAsString();
        Document cont = Jsoup.parse(content);
        
        String user = cont.getElementsByTag("target").text().substring(2);
        SkypeUserInternal skypeUser = (SkypeUserInternal) ezSkype.getSkypeUser(user);
        
        conversation.getUsers().remove(skypeUser);
        conversation.getAdmins().remove(skypeUser);
        if (skypeUser.getUsername().equals(ezSkype.getLocalUser().getUsername())) {
            String username = cont.getElementsByTag("initiator").text();
            SkypeUserInternal initiator = (SkypeUserInternal) ezSkype.getSkypeUser(username);
            SkypeConversationKickedFromEvent kickedFromEvent = new SkypeConversationKickedFromEvent(conversation, initiator);
            ezSkype.getEventManager().fire(kickedFromEvent);
        } else {
            SkypeConversationUserLeaveEvent leaveEvent = new SkypeConversationUserLeaveEvent(conversation, skypeUser);
            ezSkype.getEventManager().fire(leaveEvent);
        }
    }
}
