package in.kyle.ezskypeezlife.internal.caches;

import in.kyle.ezskypeezlife.EzSkype;
import lombok.Getter;

/**
 * Created by Kyle on 10/7/2015.
 */
public class SkypeCacheManager {
    
    @Getter
    private SkypeConversationsCache conversationsCache;
    @Getter
    private SkypeUsersCache usersCache;
    
    public SkypeCacheManager(EzSkype ezSkype) {
        this.conversationsCache = new SkypeConversationsCache(ezSkype);
        this.usersCache = new SkypeUsersCache(ezSkype);
    }
}
