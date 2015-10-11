package in.kyle.ezskypeezlife.internal.caches;

import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.internal.obj.SkypeUserInternal;
import in.kyle.ezskypeezlife.internal.packet.user.SkypeGetUserInfoPacket;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Kyle on 10/7/2015.
 */
public class SkypeUsersCache {
    
    private Set<SkypeUserInternal> skypeUsers;
    private EzSkype ezSkype;
    
    public SkypeUsersCache(EzSkype ezSkype) {
        this.ezSkype = ezSkype;
        this.skypeUsers = new HashSet<>();
    }
    
    /**
     * Get a user from the cache or get it from the server
     *
     * @param username - The username of the user
     * @return - The Skype user
     */
    public SkypeUserInternal getOrCreateUser(String username) {
        if (username.startsWith("8:")) {
            username = username.substring(2);
        }
        final String finalUsername = username;
        Optional<SkypeUserInternal> any = skypeUsers.stream().filter(skypeUserInternal -> skypeUserInternal.getUsername().equals
                (finalUsername)).findAny();
        if (!any.isPresent()) {
            SkypeGetUserInfoPacket getUserInfoPacket = new SkypeGetUserInfoPacket(ezSkype, username);
            try {
                SkypeUserInternal skypeUserInternal = (SkypeUserInternal) getUserInfoPacket.executeSync();
                this.skypeUsers.add(skypeUserInternal);
                return skypeUserInternal;
            } catch (Exception e) {
                e.printStackTrace();
                return new SkypeUserInternal(username, ezSkype);
            }
        } else {
            return any.get();
        }
    }
    
    public Set<SkypeUserInternal> getSkypeUsers() {
        return skypeUsers;
    }
}
