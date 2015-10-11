package in.kyle.ezskypeezlife.internal.thread.poll;

import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.events.conversation.SkypeConversationAddedToEvent;
import in.kyle.ezskypeezlife.events.conversation.SkypeConversationUserJoinEvent;
import in.kyle.ezskypeezlife.internal.obj.SkypeGroupConversationInternal;
import in.kyle.ezskypeezlife.internal.obj.SkypeUserInternal;
import in.kyle.ezskypeezlife.internal.thread.SkypePollMessageType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by Kyle on 10/9/2015.
 */
public class SkypeThreadActivityAddMemberType extends SkypePollMessageType {
    
    @Override
    public boolean accept(String messageType) {
        return messageType.equals("ThreadActivity/AddMember");
    }
    
    @Override
    public void extract(EzSkype ezSkype, JsonObject jsonObject, JsonObject resource) throws Exception {
        String longId = getConversationLongId(resource);
        SkypeGroupConversationInternal conversation = (SkypeGroupConversationInternal) ezSkype.getSkypeConversation(longId);
        
        String content = resource.get("content").getAsString();
        Document cont = Jsoup.parse(content);
        
        String user = cont.getElementsByTag("target").text().substring(2);
        SkypeUserInternal skypeUser = (SkypeUserInternal) ezSkype.getSkypeUser(user);
        
        conversation.getUsers().add(skypeUser);
        
        if (skypeUser.getUsername().equals(ezSkype.getLocalUser().getUsername())) {
            String username = cont.getElementsByTag("initiator").text();
            SkypeUserInternal initiator = (SkypeUserInternal) ezSkype.getSkypeUser(username);
            SkypeConversationAddedToEvent addedToEvent = new SkypeConversationAddedToEvent(skypeUser, conversation, initiator);
            ezSkype.getEventManager().fire(addedToEvent);
        } else {
            SkypeConversationUserJoinEvent joinEvent = new SkypeConversationUserJoinEvent(conversation, skypeUser);
            ezSkype.getEventManager().fire(joinEvent);
        }
    }
}
