package in.kyle.ezskypeezlife.internal.packet.conversation;

import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.internal.packet.HTTPRequest;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;

import java.io.IOException;

/**
 * Created by Kyle on 10/20/2015.
 */
public class SkypeConversationJoinPacket extends SkypePacket {
    
    public SkypeConversationJoinPacket(EzSkype ezSkype, String longId) {
        super("https://api.scheduler.skype.com/conversation/" + longId, HTTPRequest.GET, ezSkype, true);
    }
    
    @Override
    protected Object run(WebConnectionBuilder webConnectionBuilder) throws IOException {
        return EzSkype.GSON.fromJson(webConnectionBuilder.send(), JsonObject.class);
    }
}
