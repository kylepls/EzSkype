package in.kyle.ezskypeezlife.api.obj;

import in.kyle.ezskypeezlife.api.SkypeConversationType;

import java.util.List;

/**
 * Created by Kyle on 10/10/2015.
 */
public interface SkypeConversation {
    
    String getLongId();
    
    String getTopic();
    
    boolean isHistoryEnabled();
    
    boolean isJoinEnabled();
    
    String getPictureUrl();
    
    SkypeConversationType getConversationType();
    
    List<SkypeUser> getUsers();
    
    SkypeMessage sendMessage(String message);
}
