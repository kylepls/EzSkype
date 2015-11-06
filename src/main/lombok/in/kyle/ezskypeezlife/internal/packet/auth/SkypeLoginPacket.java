package in.kyle.ezskypeezlife.internal.packet.auth;

import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.api.SkypeCredentials;
import in.kyle.ezskypeezlife.api.captcha.SkypeCaptcha;
import in.kyle.ezskypeezlife.api.captcha.SkypeCaptchaHandler;
import in.kyle.ezskypeezlife.internal.exception.SkypeLoginException;
import in.kyle.ezskypeezlife.internal.exception.SkypeUnknownLoginErrorException;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

/**
 * Created by Kyle on 10/5/2015.
 */
public class SkypeLoginPacket extends SkypePacket {
    
    private final SkypeCredentials skypeCredentials;
    private final SkypeLoginJavascriptParameters javascriptParameters;
    private final Optional<SkypeCaptcha> captcha;
    
    public SkypeLoginPacket(EzSkype ezSkype, SkypeCredentials credentials, SkypeLoginJavascriptParameters javascriptParameters, 
                            SkypeCaptcha skypeCaptcha) {
        super("https://login.skype.com/login?client_id=578134&redirect_uri=https%3A%2F%2Fweb.skype.com", WebConnectionBuilder.HTTPRequest
                .POST, ezSkype, false);
        this.skypeCredentials = credentials;
        this.javascriptParameters = javascriptParameters;
        this.captcha = Optional.ofNullable(skypeCaptcha);
    }
    
    public SkypeLoginPacket(EzSkype ezSkype, SkypeCredentials credentials, SkypeLoginJavascriptParameters javascriptParameters) {
        this(ezSkype, credentials, javascriptParameters, null);
    }
    
    @Override
    protected String run(WebConnectionBuilder webConnectionBuilder) throws Exception {
        Date date = new Date();
        webConnectionBuilder.setRequest(WebConnectionBuilder.HTTPRequest.POST);
        webConnectionBuilder.addEncodedPostData("username", skypeCredentials.getUsername());
        webConnectionBuilder.addEncodedPostData("password", new String(skypeCredentials.getPassword()));
        webConnectionBuilder.addEncodedPostData("timezone_field", new SimpleDateFormat("XXX").format(date).replace(':', '|'));
        webConnectionBuilder.addEncodedPostData("pie", javascriptParameters.getPie());
        webConnectionBuilder.addEncodedPostData("etm", javascriptParameters.getEtm());
        webConnectionBuilder.addEncodedPostData("js_time", Long.toString(date.getTime() / 1000));
        webConnectionBuilder.addEncodedPostData("redirect_uri", "https://web.skype.com");
    
        if (captcha.isPresent()) {
            EzSkype.LOGGER.debug("Setting captcha parameters");
            SkypeCaptcha skypeCaptcha = captcha.get();
            webConnectionBuilder.addEncodedPostData("&hip_solution", skypeCaptcha.getSolution());
            webConnectionBuilder.addEncodedPostData("&hip_token", skypeCaptcha.getToken());
            webConnectionBuilder.addEncodedPostData("&fid", skypeCaptcha.getFid());
            webConnectionBuilder.addEncodedPostData("&hip_type", "visual");
            webConnectionBuilder.addEncodedPostData("&captcha_provider", "Hip");
        }
        
        Document document = webConnectionBuilder.getAsDocument();
        Elements elementsByAttributeValue = document.getElementsByAttributeValue("name", "skypetoken");
    
        if (elementsByAttributeValue.size() == 0) {
            Elements messageError = document.getElementsByClass("message_error");
            if (messageError.size() != 0) {
    
                if (document.html().contains("https://client.hip.live.com/GetHIP/")) {
                    EzSkype.LOGGER.debug("Has captcha");
                    if (captcha.isPresent()) {
                        EzSkype.LOGGER.debug("Captcha failed, try again");
                    }
        
                    SkypeCaptchaHandler captchaHandler = ezSkype.getCaptchaHandler();
                    if (captchaHandler != null) {
                        SkypeCaptcha skypeCaptcha = SkypeCaptchaUtils.getCaptcha(document);
                        String solution = captchaHandler.solve(skypeCaptcha);
                        skypeCaptcha.setSolution(solution);
            
                        SkypeLoginPacket loginPacket = new SkypeLoginPacket(ezSkype, skypeCredentials, javascriptParameters, skypeCaptcha);
                        return (String) loginPacket.executeSync();
                    } else {
                        EzSkype.LOGGER.error("Cannot solve Skype captcha, captcha handler is null");
                    }
                }
    
                SkypeLoginException skypeLoginException = new SkypeLoginException(messageError.text(), document.html());
                EzSkype.LOGGER.error("Error logging in", skypeLoginException);
                throw skypeLoginException;
            } else {
                SkypeUnknownLoginErrorException skypeUnknownLoginErrorException = new SkypeUnknownLoginErrorException(document.toString()
                        , document.html());
                EzSkype.LOGGER.error("Unknown login error", skypeUnknownLoginErrorException);
                throw skypeUnknownLoginErrorException;
            }
        }
    
        String value = elementsByAttributeValue.get(0).val();
        EzSkype.LOGGER.debug("Got login value: " + value);
        return value;
    }
}
