package in.kyle.ezskypeezlife.internal.thread.poll;

import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.internal.thread.SkypePollMessageType;

/**
 * Created by Kyle on 10/9/2015.
 */
public class SkypeControlClearTypingType extends SkypePollMessageType {
    
    @Override
    public boolean accept(String messageType) {
        return messageType.equals("Control/ClearTyping");
    }
    
    @Override
    public void extract(EzSkype ezSkype, JsonObject jsonObject, JsonObject resource) throws Exception {
        // TODO add
    }
}
