package in.kyle.ezskypeezlife.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Kyle on 10/7/2015.
 */
/**
 * Skype permissions
 * 
 */
@AllArgsConstructor
public enum SkypeConversationPermission {
    
    ADD_MEMBER("AddMember"),
    CHANGE_TOPIC("ChangeTopic"),
    CHANGE_PICTURE("ChangePicture"),
    EDIT_MESSAGE("EditMsg"),
    CALL_P2P("CallP2P"),
    SEND_TEXT("SendText"),
    SEND_SMS("SendSms"),
    SEND_FILE_P2P("SendFileP2P"),
    SEND_CONTACTS("SendContacts"),
    SEND_VIDEO_MESSAGE("SendVideoMsg"),
    SEND_MEDIA_MESSAGE("SendMediaMsg");
    
    @Getter
    private final String skypeString;
    
    public static SkypeConversationPermission getFromSkypeString(String skypeString) {
        for (SkypeConversationPermission permission : values()) {
            if (permission.getSkypeString().equals(skypeString)) {
                return permission;
            }
        }
        return null;
    }
}
