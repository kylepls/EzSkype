package in.kyle.ezskypeezlife.internal.obj;

import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.api.conversation.message.SkypeFlik;
import in.kyle.ezskypeezlife.api.conversation.message.SkypeFlikMessage;
import in.kyle.ezskypeezlife.api.conversation.message.SkypeMessageType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by Kyle on 12/6/2015.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class SkypeFlikMessageInternal extends SkypeMessageInternal implements SkypeFlikMessage {
    
    private final SkypeFlik flik;
    
    public SkypeFlikMessageInternal(EzSkype ezSkype, String id, SkypeUserInternal sender, boolean edited, SkypeMessageType messageType, 
                                    String message, SkypeConversationInternal conversation, SkypeFlik skypeFlik) {
        super(ezSkype, id, sender, edited, messageType, message, conversation);
        this.flik = skypeFlik;
    }
}
