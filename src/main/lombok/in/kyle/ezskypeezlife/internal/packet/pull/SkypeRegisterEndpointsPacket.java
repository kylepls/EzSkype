package in.kyle.ezskypeezlife.internal.packet.pull;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.api.skype.SkypeEndpoint;
import in.kyle.ezskypeezlife.internal.packet.ContentType;
import in.kyle.ezskypeezlife.internal.packet.HTTPRequest;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Kyle on 10/7/2015.
 * <p>
 * Used to register Skype endpoints for polling
 */
public class SkypeRegisterEndpointsPacket extends SkypePacket {
    
    private final SkypeEndpoint[] endpoints;
    
    public SkypeRegisterEndpointsPacket(EzSkype ezSkype, SkypeEndpoint[] endpoints) {
        super("https://client-s.gateway.messenger.live.com/v1/users/ME/endpoints/SELF/subscriptions", HTTPRequest
                .POST, ezSkype, true);
        this.endpoints = endpoints;
    }
    
    @Override
    protected Object run(WebConnectionBuilder webConnectionBuilder) throws IOException {
        
        JsonObject data = new JsonObject();
        data.addProperty("channelType", "httpLongPoll");
        data.addProperty("template", "raw");
        JsonArray resources = new JsonArray();
        Arrays.stream(endpoints).forEach(endpoint -> resources.add(new JsonPrimitive(endpoint.getUrlAppend())));
        data.add("interestedResources", resources);
    
        EzSkype.LOGGER.debug("Endpoints: {}", Arrays.asList(endpoints));
        EzSkype.LOGGER.debug("Data {}", data);
        webConnectionBuilder.locationPrefix(ezSkype.getSkypeSession().getLocation());
        webConnectionBuilder.setContentType(ContentType.JSON);
        webConnectionBuilder.setPostData(data.toString());
        
        webConnectionBuilder.send();
        return null;
    }
}
