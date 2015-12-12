package in.kyle.ezskypeezlife.internal.obj;

import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.api.SkypeConversationType;
import in.kyle.ezskypeezlife.api.obj.SkypeUser;
import in.kyle.ezskypeezlife.api.obj.SkypeUserConversation;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Arrays;
import java.util.Collections;

/**
 * Created by Kyle on 10/7/2015.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SkypeUserConversationInternal extends SkypeConversationInternal implements SkypeUserConversation {
    
    private SkypeUserInternal participant;
    
    // @formatter:off
    public SkypeUserConversationInternal(
            EzSkype ezSkype,
            String longId,
            String topic,
            boolean historyEnabled,
            boolean joinEnabled,
            String url
    ) {
        super(longId, SkypeConversationType.USER, ezSkype, topic, historyEnabled, joinEnabled, url, Collections.emptyList(), true);
        participant = (SkypeUserInternal) ezSkype.getSkypeUser(longId.substring(longId.indexOf(":")+1));
        setUsers(Arrays.asList(participant, 
                (SkypeUserInternal) ezSkype.getLocalUser()));
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
    
    @Override
    public void fullyLoad() {
    }
    
    @Override
    public boolean isLoaded() {
        return true;
    }
    
    @Override
    public void changeTopic(String topic) {
        throw new IllegalStateException("Cannot set topic for user conversation");
    }
    
    @Override
    public SkypeUser getParticipant() {
        return participant;
    }
}
