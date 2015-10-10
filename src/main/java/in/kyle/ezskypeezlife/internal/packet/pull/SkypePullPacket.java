package in.kyle.ezskypeezlife.internal.packet.pull;

import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

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
            e.printStackTrace();
            HttpURLConnection connection = webConnectionBuilder.getConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
        JsonObject response = EzSkype.GSON.fromJson(result, JsonObject.class);
        return response;
    }
}
