package in.kyle.ezskypeezlife.internal.packet.pull;

import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;
import org.apache.commons.io.IOUtils;

import java.io.FileNotFoundException;
import java.io.StringWriter;

/**
 * Created by Kyle on 10/6/2015.
 * <p>
 * Used to pull new events from the Skype server
 * Used explicitly in the Skype poller class
 */
public class SkypePullPacket extends SkypePacket {
    
    public SkypePullPacket(EzSkype ezSkype) {
        super("https://client-s.gateway.messenger.live.com/v1/users/ME/endpoints/SELF/subscriptions/0/poll", WebConnectionBuilder
                .HTTPRequest.POST, ezSkype, true);
    }
    
    @Override
    protected JsonObject run(WebConnectionBuilder webConnectionBuilder) throws Exception {
        webConnectionBuilder.setContentType(WebConnectionBuilder.ContentType.JSON);
        String result = "";
        try {
            result = webConnectionBuilder.send();
        } catch (FileNotFoundException e) {
            StringWriter writer = new StringWriter();
            IOUtils.copy(webConnectionBuilder.getConnection().getErrorStream(), writer);
            String string = writer.toString();
            
            EzSkype.LOGGER.error("Error pulling Skype info: \n" + string, e);
        }
        return EzSkype.GSON.fromJson(result, JsonObject.class);
    }
}
