package in.kyle.ezskypeezlife.internal.obj;


import lombok.Getter;

import java.util.LinkedHashMap;

/**
 * Created by Kyle on 10/27/2015.
 */
public class SkypeMessageCacheInternal {
    
    @Getter
    private final LinkedHashMap<String, SkypeMessageInternal> skypeMessages;
    
    public SkypeMessageCacheInternal() {
        this.skypeMessages = new LinkedHashMap<>();
    }
    
    public SkypeMessageInternal getSkypeMessage(String id) {
        return skypeMessages.get(id);
    }
    
    public boolean hasSkypeMessage(String id) {
        return skypeMessages.containsKey(id);
    }
    
    public void addMessage(SkypeMessageInternal skypeMessage) {
        skypeMessages.put(skypeMessage.getId(), skypeMessage);
    }
}
