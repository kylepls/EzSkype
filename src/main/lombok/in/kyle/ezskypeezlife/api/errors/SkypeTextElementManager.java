package in.kyle.ezskypeezlife.api.errors;

import in.kyle.ezskypeezlife.api.conversation.message.SkypeEmoji;
import in.kyle.ezskypeezlife.api.conversation.message.SkypeFlik;
import in.kyle.ezskypeezlife.api.conversation.message.SkypeMessageElement;

import java.util.List;
import java.util.Optional;

/**
 * Created by Kyle on 12/12/2015.
 */
public interface SkypeTextElementManager {
    
    List<SkypeEmoji> getEmojis();
    
    List<SkypeFlik> getFliks();
    
    Optional<SkypeEmoji> getEmoji(String shortcut);
    
    Optional<SkypeMessageElement> getElement(String id);
    
}
