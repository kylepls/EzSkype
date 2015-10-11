package in.kyle.ezskypeezlife.internal.obj;

import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.api.SkypeConversationType;

import java.util.Collections;

/**
 * Created by Kyle on 10/7/2015.
 */
public class SkypeConversationInternalEmpty extends SkypeConversationInternal {
    
    public SkypeConversationInternalEmpty(EzSkype ezSkype, String longId) {
        super(ezSkype, longId, "", false, false, null, SkypeConversationType.GROUP, Collections.EMPTY_LIST);
    }
}