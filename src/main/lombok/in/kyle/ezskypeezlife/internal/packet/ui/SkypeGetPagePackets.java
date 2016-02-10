package in.kyle.ezskypeezlife.internal.packet.ui;

import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.api.skype.SkypeData;
import in.kyle.ezskypeezlife.exception.SkypeException;
import in.kyle.ezskypeezlife.internal.obj.SkypeLyncExperiences;
import in.kyle.ezskypeezlife.internal.obj.SkypeTextElementManagerInternal;
import in.kyle.ezskypeezlife.internal.packet.HTTPRequest;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Kyle on 12/7/2015.
 */
public class SkypeGetPagePackets extends SkypePacket {
    
    private final Pattern bootStrapPattern;
    private final Pattern platformPattern;
    private final Pattern versionPattern;
    
    public SkypeGetPagePackets(EzSkype ezSkype) {
        super("https://web.skype.com/en/", HTTPRequest.GET, ezSkype, false);
        bootStrapPattern = Pattern.compile("swx\\.cdn\\.skype\\.com\\/shared\\/v\\/([0-9\\.]{3,9})");
        platformPattern = Pattern.compile("platformId:([0-9]{0,5})");
        versionPattern = Pattern.compile("([0-9]\\.)+[0-9]");
    }
    
    @Override
    protected SkypeData run(WebConnectionBuilder webConnectionBuilder) throws IOException, SkypeException {
        Document document = webConnectionBuilder.getAsDocument();
        
        String javascriptUrl = document.getElementsByTag("script").attr("src");
        webConnectionBuilder.setUrl(javascriptUrl);
    
        logger.debug("Getting javascript from {}", javascriptUrl);
        
        String javascript = webConnectionBuilder.send();
        
        Matcher matcher = bootStrapPattern.matcher(javascript);
        if (matcher.find()) {
            String url = "http://" + matcher.group() + "/SkypeBootstrap.min.js";
    
            logger.debug("Got javascript from {}", url);
            
            webConnectionBuilder.setUrl(url);
            String bootStrapJavascript = webConnectionBuilder.send();
    
            logger.debug("  BS javascript: {}", bootStrapJavascript);
            matcher = platformPattern.matcher(bootStrapJavascript);
            if (matcher.find()) {
                int platformId = Integer.parseInt(matcher.group(1));
                matcher = versionPattern.matcher(bootStrapJavascript);
                if (matcher.find()) {
                    String version = matcher.group();
                    
                    SkypeLyncExperiences skypeLyncExperiences = (SkypeLyncExperiences) new SkypeGetLincExperiencePacket(ezSkype, 
                            platformId, version).executeSync();
                    
                    SkypeGetPesConfigPacket.PesReturnObject pesConfig = (SkypeGetPesConfigPacket.PesReturnObject) new 
                            SkypeGetPesConfigPacket(ezSkype, skypeLyncExperiences.getPlatformId(), skypeLyncExperiences.getSwxVersion())
                            .executeSync();
    
                    return new SkypeData(document, javascript, bootStrapJavascript, version, pesConfig.getPesConfigUrl(), new 
                            SkypeTextElementManagerInternal(pesConfig.getData()));
                } else {
                    throw new SkypeException("Could not get Skype Lync version, please try again later. If the error persists, please " +
                            "open an issue on Github");
                }
            } else {
                throw new SkypeException("Could not get Skype Lync platform version, please try again later. If the error persists, " + 
                        "please open an issue on Github");
            }
        } else {
            throw new SkypeException("Could not get Skype javascript, please try again later. If the error persists, please open" + "an " +
                    "issue on Github");
        }
    }
}
