package in.kyle.ezskypeezlife.internal.obj;

import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.api.SkypeConversationType;
import in.kyle.ezskypeezlife.api.obj.SkypeUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;

/**
 * Created by Kyle on 10/7/2015.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SkypeUserConversationInternal extends SkypeConversationInternal {
    
    // @formatter:off
    public SkypeUserConversationInternal(
            EzSkype ezSkype,
            String longId,
            String topic,
            boolean historyEnabled,
            boolean joinEnabled,
            String url
    ) {
        super(ezSkype, longId, topic, historyEnabled, joinEnabled, url, SkypeConversationType.USER, new ArrayList<>());
    }
    // @formatter:on
    
    @Override
    public boolean kick(SkypeUser skypeUser) {
        return false;
    }
    
    @Override
    public boolean isAdmin(SkypeUser skypeUser) {
        return true;
    }
    
    @Override
    public String getJoinUrl() throws Exception {
        return "";
    }
}
