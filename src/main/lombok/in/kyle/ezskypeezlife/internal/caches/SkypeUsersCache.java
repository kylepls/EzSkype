package in.kyle.ezskypeezlife.internal.caches;

import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.api.obj.SkypeUser;
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
    public SkypeUserInternal getOrCreateUserLoaded(String username) {
        if (username.startsWith("8:")) {
            username = username.substring(2);
        }
        final String finalUsername = username;
        Optional<SkypeUserInternal> any = skypeUsers.stream().filter(skypeUserInternal -> skypeUserInternal.getUsername().equals
                (finalUsername)).findAny();
        if (any.isPresent()) {
            return any.get();
        } else {
            SkypeGetUserInfoPacket getUserInfoPacket = new SkypeGetUserInfoPacket(ezSkype, username);
            try {
                SkypeUserInternal skypeUserInternal = (SkypeUserInternal) getUserInfoPacket.executeSync();
                this.skypeUsers.add(skypeUserInternal);
                return skypeUserInternal;
            } catch (Exception e) {
                e.printStackTrace();
                return new SkypeUserInternal(username, ezSkype);
            }
        }
    }
    
    /**
     * Creates a new Skype user that is unloaded
     * @param username - The username to get
     * @return - The unloaded Skype user
     */
    public SkypeUserInternal getOrCreateUserUnloaded(String username) {
        if (username.startsWith("8:")) {
            username = username.substring(2);
        }
        final String finalUsername = username;
        Optional<SkypeUserInternal> any = skypeUsers.stream().filter(skypeUserInternal -> skypeUserInternal.getUsername().equals
                (finalUsername)).findAny();
        if (any.isPresent()) {
            return any.get();
        } else {
            return new SkypeUserInternal(username, ezSkype);
        }
    }
    
    /**
     * Fully load a user if not already fully loaded
     * @param skypeUser - The user to load
     * @return - The loaded Skype user
     */
    public SkypeUser fullyLoadUser(SkypeUserInternal skypeUser) {
        SkypeGetUserInfoPacket getUserInfoPacket = new SkypeGetUserInfoPacket(ezSkype, skypeUser.getUsername());
        SkypeUserInternal skypeUserNew;
        try {
            skypeUserNew = (SkypeUserInternal) getUserInfoPacket.executeSync();
        } catch (Exception e) {
            e.printStackTrace();
            return skypeUser;
        }
    
        skypeUser.setFirstName(skypeUserNew.getFirstName());
        skypeUser.setLastName(skypeUserNew.getLastName());
        skypeUser.setMood(skypeUserNew.getMood());
        skypeUser.setRichMood(skypeUserNew.getRichMood());
        skypeUser.setDisplayName(skypeUserNew.getDisplayName());
        skypeUser.setCountry(skypeUserNew.getCountry());
        skypeUser.setCity(skypeUserNew.getCity());
        skypeUser.setAvatarUrl(skypeUserNew.getAvatarUrl());
        skypeUser.setLoaded(true);
        return skypeUser;
    }
    
    public Set<SkypeUserInternal> getSkypeUsers() {
        return skypeUsers;
    }
}
