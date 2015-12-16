package in.kyle.ezskypeezlife.internal.packet.ui;

import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.exception.SkypeException;
import in.kyle.ezskypeezlife.internal.obj.SkypeLyncExperiences;
import in.kyle.ezskypeezlife.internal.packet.HTTPRequest;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Kyle on 12/7/2015.
 */
public class SkypeGetLincExperiencePacket extends SkypePacket {
    
    private final Pattern swxPlatformIdPattern;
    
    public SkypeGetLincExperiencePacket(EzSkype ezSkype, int platformId, String version) {
        super("https://b.config.skype.com/config/v1/SkypeLyncWebExperience/" + platformId + "_" + version + ".0?apikey=skype.com", 
                HTTPRequest.GET, ezSkype, false);
        swxPlatformIdPattern = Pattern.compile("platformId:(?: +)?(?:\"|')?([0-9]{3,4})");
    }
    
    @Override
    protected SkypeLyncExperiences run(WebConnectionBuilder webConnectionBuilder) throws IOException, SkypeException {
        
        JsonObject lync = EzSkype.GSON.fromJson(webConnectionBuilder.send(), JsonObject.class).getAsJsonObject("SkypeLyncWebExperience");
        
        String baseUrl = lync.get("appBaseUrl").getAsString();
        String swxVersion = baseUrl.substring(baseUrl.lastIndexOf("/") + 1);
        String swxJavascriptUrl = lync.get("packageUrl").getAsString();
        
        webConnectionBuilder.setUrl(swxJavascriptUrl);
        String swxJavascript = webConnectionBuilder.send();
        Matcher matcher = swxPlatformIdPattern.matcher(swxJavascript);
        
        if (matcher.find()) {
            int platformId = Integer.parseInt(matcher.group(1));
            return new SkypeLyncExperiences(baseUrl, swxVersion, platformId);
            
        } else {
            EzSkype.LOGGER.error("BASE URL: " + baseUrl);
            EzSkype.LOGGER.error("SWX JAVASCRIPT URL: " + swxJavascriptUrl);
            EzSkype.LOGGER.error("SWX: \n" + swxJavascript);
            throw new SkypeSwxVersionUndefinedException();
        }
        
    }
}
