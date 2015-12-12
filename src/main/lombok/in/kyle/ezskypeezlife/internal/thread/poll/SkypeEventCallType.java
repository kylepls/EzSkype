package in.kyle.ezskypeezlife.internal.thread.poll;

import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.events.conversation.SkypeConversationCallEndedEvent;
import in.kyle.ezskypeezlife.events.conversation.SkypeConversationCallStartedEvent;
import in.kyle.ezskypeezlife.internal.obj.SkypeGroupConversationInternal;
import in.kyle.ezskypeezlife.internal.obj.SkypeUserInternal;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by Kyle on 10/9/2015.
 */
public class SkypeEventCallType extends SkypePollMessageType {
    
    @Override
    public boolean accept(String messageType) {
        return messageType.equals("Event/Call");
    }
    
    @Override
    public void extract(EzSkype ezSkype, JsonObject jsonObject, JsonObject resource) throws Exception {
        String content = resource.get("content").getAsString();
    
        String longId = getConversationLongId(resource);
        SkypeGroupConversationInternal conversation = (SkypeGroupConversationInternal) ezSkype.getSkypeConversation(longId);
        
        Document document = Jsoup.parse(content);
        
        String type = document.getElementsByTag("partlist").get(0).attr("type");
        
        String username = document.getElementsByAttribute("identity").get(0).attr("identity");
        SkypeUserInternal skypeUserInternal = (SkypeUserInternal) ezSkype.getSkypeUser(username);
        
        if (type.equals("started")) {
            SkypeConversationCallStartedEvent callEvent = new SkypeConversationCallStartedEvent(conversation, skypeUserInternal);
            ezSkype.getEventManager().fire(callEvent);
        } else if (type.equals("ended")) {
            SkypeConversationCallEndedEvent callEndedEvent = new SkypeConversationCallEndedEvent(skypeUserInternal, conversation);
            ezSkype.getEventManager().fire(callEndedEvent);
        }
    }
}
