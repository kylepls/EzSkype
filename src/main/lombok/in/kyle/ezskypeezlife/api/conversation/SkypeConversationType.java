package in.kyle.ezskypeezlife.api.conversation;

/**
 * Created by Kyle on 10/7/2015.
 */
public enum SkypeConversationType {
    
    GROUP, USER;
    
    public static SkypeConversationType fromLongId(String longId) {
        if (longId.startsWith("8:")) {
            return USER;
        } else {
            return GROUP;
        }
    }
}
