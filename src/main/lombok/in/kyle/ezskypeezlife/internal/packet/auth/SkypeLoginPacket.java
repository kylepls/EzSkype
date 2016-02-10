package in.kyle.ezskypeezlife.internal.packet.auth;

import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.api.skype.SkypeCredentials;
import in.kyle.ezskypeezlife.exception.SkypeException;
import in.kyle.ezskypeezlife.internal.packet.HTTPRequest;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Kyle on 10/5/2015.
 */
public class SkypeLoginPacket extends SkypePacket {
    
    private static final String SKYPE_VERSION = "0/7.17.0.105//";
    private static final String AUTH_PAYLOAD = "%s\nskyper\n%s";
    private final SkypeCredentials skypeCredentials;
    
    public SkypeLoginPacket(EzSkype ezSkype, SkypeCredentials skypeCredentials) {
        super("https://api.skype.com/login/skypetoken", HTTPRequest.POST, ezSkype, false);
        this.skypeCredentials = skypeCredentials;
    }
    
    @Override
    @SneakyThrows
    protected Object run(WebConnectionBuilder webConnectionBuilder) throws SkypeException, IOException {
        webConnectionBuilder.addEncodedPostData("scopes", "client");
        webConnectionBuilder.addEncodedPostData("clientVersion", SKYPE_VERSION);
        webConnectionBuilder.addEncodedPostData("username", skypeCredentials.getUsername());
        
        String hash = hash(skypeCredentials.getUsername(), new String(skypeCredentials.getPassword()));
        
        webConnectionBuilder.addEncodedPostData("passwordHash", hash);
        
        JsonObject response = webConnectionBuilder.getAsJsonObject();
        return response.get("skypetoken").getAsString();
    }
    
    public String hash(String username, String password) throws IllegalStateException {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            
            byte[] encodedMD = md.digest(String.format(AUTH_PAYLOAD, username, password).getBytes());
            byte[] encodedBase = Base64.encodeBase64(encodedMD);
            
            return new String(encodedBase);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
    
    /*
    public SkypeLoginPacket(EzSkype ezSkype, SkypeCredentials credentials, SkypeJavascriptParams javascriptParameters, SkypeCaptcha 
            skypeCaptcha) {
        super("https://login.skype.com/login?client_id=578134&redirect_uri=https%3A%2F%2Fweb.skype.com", HTTPRequest.POST, ezSkype, false);
        this.skypeCredentials = credentials;
        this.javascriptParameters = javascriptParameters;
        this.captcha = Optional.ofNullable(skypeCaptcha);
    }
     */
    
    /*
    @Override
    protected String run(WebConnectionBuilder webConnectionBuilder) throws SkypeException, IOException {
        Date date = new Date();
        webConnectionBuilder.setRequest(HTTPRequest.POST);
        webConnectionBuilder.addEncodedPostData("username", skypeCredentials.getUsername());
        webConnectionBuilder.addEncodedPostData("password", new String(skypeCredentials.getPassword()));
        webConnectionBuilder.addEncodedPostData("timezone_field", new SimpleDateFormat("XXX").format(date).replace(':', '|'));
        webConnectionBuilder.addEncodedPostData("pie", javascriptParameters.getPie());
        webConnectionBuilder.addEncodedPostData("etm", javascriptParameters.getEtm());
        webConnectionBuilder.addEncodedPostData("js_time", Long.toString(date.getTime() / 1000));
        webConnectionBuilder.addEncodedPostData("redirect_uri", "https://web.skype.com");
    
        if (captcha.isPresent()) {
            EzSkype.LOGGER.debug("Setting errors parameters");
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
            if (document.text().contains("For your security, you must change your password before you proceed.")) {
                EzSkype.LOGGER.debug("Change password");
            
                SkypeErrorHandler errorHandler = ezSkype.getErrorHandler();
                if (errorHandler != null) {
                    String newPassword = errorHandler.setNewPassword();
                
                    if (newPassword == null || newPassword.isEmpty()) {
                        throw new SkypeChangePasswordEmptyException(newPassword);
                    }
                
                    SkypeChangePasswordPacket skypeChangePasswordPacket = new SkypeChangePasswordPacket(ezSkype, SkypeLoginUtils
                            .getParameters(document), ezSkype.getSkypeCredentials().getUsername(), newPassword);
                
                    ezSkype.getSkypeCredentials().setPassword(newPassword.toCharArray());
                
                    skypeChangePasswordPacket.executeSync();
                
                    SkypeLoginPacket loginPacket = new SkypeLoginPacket(ezSkype, skypeCredentials, javascriptParameters);
                    return (String) loginPacket.executeSync();
                } else {
                    EzSkype.LOGGER.error("Cannot solve Skype errors, errors handler is null");
                    throw new SkypeChangePasswordException(document.html());
                }
            } else if (document.html().contains("https://client.hip.live.com/GetHIP/")) {
                EzSkype.LOGGER.debug("Has errors");
                if (captcha.isPresent()) {
                    EzSkype.LOGGER.debug("Captcha failed, try again");
                }
            
                SkypeErrorHandler errorHandler = ezSkype.getErrorHandler();
                if (errorHandler != null) {
                    SkypeCaptcha skypeCaptcha = SkypeCaptchaUtils.getCaptcha(document);
                    String solution = errorHandler.solve(skypeCaptcha);
                    skypeCaptcha.setSolution(solution);
                
                    SkypeLoginPacket loginPacket = new SkypeLoginPacket(ezSkype, skypeCredentials, javascriptParameters, skypeCaptcha);
                    return (String) loginPacket.executeSync();
                } else {
                    EzSkype.LOGGER.error("Cannot solve Skype errors, errors handler is null");
                    throw new SkypeCaptchaException(document.html());
                }
            }
        
            SkypeLoginException skypeLoginException = new SkypeLoginException(document.html());
            EzSkype.LOGGER.error("Error logging in", skypeLoginException);
            throw skypeLoginException;
        }
    
        String value = elementsByAttributeValue.get(0).val();
        EzSkype.LOGGER.debug("  Got login value: " + value);
        return value;
    }
    */
}
