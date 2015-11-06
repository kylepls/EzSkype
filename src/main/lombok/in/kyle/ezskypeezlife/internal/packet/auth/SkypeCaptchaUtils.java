package in.kyle.ezskypeezlife.internal.packet.auth;

import in.kyle.ezskypeezlife.api.captcha.SkypeCaptcha;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;
import lombok.experimental.UtilityClass;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by Kyle on 11/5/2015.
 * <p>
 * Joe's code, not mine
 */
@UtilityClass
public class SkypeCaptchaUtils {
    
    public static SkypeCaptcha getCaptcha(Document document) throws IOException {
        String url = getImageUrl(document.html());
        return new SkypeCaptcha(getToken(url), getFID(url), url);
    }
    
    public static String getImageUrl(String html) throws IOException {
        String rawUrl = html.split("var skypeHipUrl = \"")[1].split("\";")[0];
        
        WebConnectionBuilder webConnectionBuilder = new WebConnectionBuilder();
        webConnectionBuilder.setUrl(rawUrl);
        String jsText = webConnectionBuilder.send();
        
        return jsText.split("imageurl:'")[1].split("',")[0];
    }
    
    public static String getToken(String image) {
        return image.split("hid=")[1].split("&fid")[0];
    }
    
    private static String getFID(String image) {
        return image.split("fid=")[1].split("&id")[0];
    }
}
