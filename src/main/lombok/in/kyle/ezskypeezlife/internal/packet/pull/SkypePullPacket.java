package in.kyle.ezskypeezlife.internal.packet.pull;

import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.internal.packet.ContentType;
import in.kyle.ezskypeezlife.internal.packet.HTTPRequest;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;
import org.apache.commons.io.IOUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by Kyle on 10/6/2015.
 * <p>
 * Used to pull new events from the Skype server
 * Used explicitly in the Skype poller class
 */
public class SkypePullPacket extends SkypePacket {
    
    public SkypePullPacket(EzSkype ezSkype) {
        super("https://client-s.gateway.messenger.live.com/v1/users/ME/endpoints/SELF/subscriptions/0/poll", HTTPRequest.POST, ezSkype, 
                true);
    }
    
    @Override
    protected JsonObject run(WebConnectionBuilder webConnectionBuilder) throws IOException {
        webConnectionBuilder.setContentType(ContentType.JSON);
        String result = "";
        try {
            result = webConnectionBuilder.send();
        } catch (FileNotFoundException e) {
            StringWriter writer = new StringWriter();
            IOUtils.copy(webConnectionBuilder.getConnection().getErrorStream(), writer);
            String string = writer.toString();
            
            EzSkype.LOGGER.error("Error pulling Skype info: \n" + string, e);
            ezSkype.getErrorHandler().handleException(e);
        }
        return EzSkype.GSON.fromJson(result, JsonObject.class);
    }
}
