package in.kyle.ezskypeezlife.internal.thread.poll;

import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.events.conversation.SkypeConversationPictureChangeEvent;
import in.kyle.ezskypeezlife.internal.obj.SkypeGroupConversationInternal;
import in.kyle.ezskypeezlife.internal.obj.SkypeUserInternal;
import in.kyle.ezskypeezlife.internal.thread.SkypePollMessageType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by Kyle on 10/9/2015.
 */
public class SkypeThreadActivityPictureUpdateType extends SkypePollMessageType {
    
    @Override
    public boolean accept(String messageType) {
        return messageType.equals("ThreadActivity/PictureUpdate");
    }
    
    @Override
    public void extract(EzSkype ezSkype, JsonObject jsonObject, JsonObject resource) throws Exception {
        
        String longId = getConversationLongId(resource);
        SkypeGroupConversationInternal conversation = (SkypeGroupConversationInternal) ezSkype.getSkypeConversation(longId);
        
        String content = resource.get("content").getAsString();
        
        Document cont = Jsoup.parse(content);
        
        String oldPicture = conversation.getPictureUrl();
        String newPicture = cont.getElementsByTag("value").text().substring(4);
        String username = cont.getElementsByTag("initiator").text();
        SkypeUserInternal user = (SkypeUserInternal) ezSkype.getSkypeUser(username);
        conversation.setPictureUrl(newPicture);
        SkypeConversationPictureChangeEvent pictureChangeEvent = new SkypeConversationPictureChangeEvent(user, conversation, oldPicture, 
                newPicture);
        ezSkype.getEventManager().fire(pictureChangeEvent);
    }
}
