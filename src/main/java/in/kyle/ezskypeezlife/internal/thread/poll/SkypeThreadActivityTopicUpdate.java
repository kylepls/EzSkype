package in.kyle.ezskypeezlife.internal.thread.poll;

import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.events.conversation.SkypeConversationUpdateTopicEvent;
import in.kyle.ezskypeezlife.internal.obj.SkypeGroupConversationInternal;
import in.kyle.ezskypeezlife.internal.obj.SkypeUserInternal;
import in.kyle.ezskypeezlife.internal.thread.SkypePollMessageType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by Kyle on 10/9/2015.
 */
public class SkypeThreadActivityTopicUpdate extends SkypePollMessageType {
    
    @Override
    public boolean accept(String messageType) {
        return messageType.equals("ThreadActivity/TopicUpdate");
    }
    
    @Override
    public void extract(EzSkype ezSkype, JsonObject jsonObject, JsonObject resource) throws Exception {
        
        String longId = getConversationLongId(resource);
        SkypeGroupConversationInternal conversation = (SkypeGroupConversationInternal) ezSkype.getSkypeConversation(longId);
        
        String content = resource.get("content").getAsString();
        Document cont = Jsoup.parse(content);
        
        String username = cont.getElementsByTag("initiator").text();
        SkypeUserInternal user = ezSkype.getSkypeUser(username);
        String oldTopic = conversation.getTopic();
        String newTopic = cont.getElementsByTag("value").text();
        SkypeConversationUpdateTopicEvent topicEvent = new SkypeConversationUpdateTopicEvent(conversation, user, oldTopic, newTopic);
        ezSkype.getEventManager().fire(topicEvent);
    }
}
