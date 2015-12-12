package in.kyle.ezskypeezlife.internal.thread.poll;

import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.api.conversation.message.SkypeFlik;
import in.kyle.ezskypeezlife.api.conversation.message.SkypeMessageElement;
import in.kyle.ezskypeezlife.events.conversation.SkypeFlikMessageReceivedEvent;
import in.kyle.ezskypeezlife.internal.obj.SkypeFlikMessageInternal;
import in.kyle.ezskypeezlife.internal.obj.SkypeMessageInternal;
import in.kyle.ezskypeezlife.internal.thread.SkypeParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Optional;

/**
 * Created by Kyle on 12/5/2015.
 */
public class SkypeMediaFlikMessageType extends SkypePollMessageType {
    
    @Override
    public boolean accept(String messageType) {
        return messageType.equals("RichText/Media_FlikMsg");
    }
    
    @Override
    public void extract(EzSkype ezSkype, JsonObject jsonObject, JsonObject resource) throws Exception {
        SkypeMessageInternal message = getMessageFromJson(ezSkype, jsonObject);
        
        Document content = Jsoup.parse(message.getMessage());
        Element uriObject = content.getElementsByTag("URIObject").get(0);
        
        String id = uriObject.attr("uri");
        id = id.substring(id.lastIndexOf("/") + 1);
        
        Optional<SkypeMessageElement> element = ezSkype.getSkypeData().getTextElementManager().getElement(id);
        
        if (element.isPresent()) {
            if (element.get() instanceof SkypeFlik) {
                SkypeFlik skypeFlik = (SkypeFlik) element.get();
                
                SkypeFlikMessageInternal flikMessageInternal = new SkypeFlikMessageInternal(ezSkype, message.getId(), message.getSender()
                        , message.isEdited(), message.getMessageType(), message.getMessage(), message.getConversation(), skypeFlik);
                
                SkypeFlikMessageReceivedEvent skypeFlikMessageReceivedEvent = new SkypeFlikMessageReceivedEvent(flikMessageInternal, 
                        message.getSender(), message.getConversation());
                
                ezSkype.getEventManager().fire(skypeFlikMessageReceivedEvent);
            } else {
                throw new SkypeParseException(jsonObject, "Incorrect type for message id " + id + ", not of type SkypeFlik, type=" +
                        element.get().getClass().getCanonicalName());
            }
        } else {
            throw new SkypeParseException(jsonObject, "Could not find flik with id " + id);
        }
    }
}
