package in.kyle.ezskypeezlife.internal.packet.auth;

import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.api.skype.SkypeCredentials;
import in.kyle.ezskypeezlife.internal.obj.SkypeSession;
import in.kyle.ezskypeezlife.internal.packet.HTTPRequest;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;
import org.apache.commons.codec.binary.Base64;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import static in.kyle.ezskypeezlife.EzSkype.LOGGER;

/**
 * Created by Kyle on 10/5/2015.
 */
public class SkypeAuthFinishPacket extends SkypePacket {
    
    private static String SKYPE_VERSION = "0/7.17.0.105//";
    private static String AUTH_PAYLOAD = "%s\nskyper\n%s";
    
    private String token;
    
    public SkypeAuthFinishPacket(EzSkype ezSkype, String token) {
        super("https://client-s.gateway.messenger.live.com/v1/users/ME/endpoints", HTTPRequest.POST, ezSkype, false);
        this.token = token;
    }
    
    @Override
    protected SkypeSession run(WebConnectionBuilder webConnectionBuilder) throws IOException {
        LOGGER.info("   Token: " + token + " throwing IT IN THE TRAAAAAAAAAAAAAAASH");
    
        LOGGER.info("   Getting new token...");
    
        SkypeCredentials skypeCredentials = ezSkype.getSkypeCredentials();
    
        try {
            token = getXTokenFromUsernamePassword(skypeCredentials.getUsername(), new String(skypeCredentials.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        LOGGER.info("   New better token is: " + token);
        
        webConnectionBuilder.addHeader("Authentication", "skypetoken=" + token);
        webConnectionBuilder.setPostData("{}");
        
        webConnectionBuilder.send();
        
        String location = webConnectionBuilder.getConnection().getHeaderField("Location");
        location = location.substring(location.indexOf("//") + 2, location.indexOf("client"));
        
        String[] tokenOre = webConnectionBuilder.getConnection().getHeaderField("Set-RegistrationToken").split(";");
        String regToken = tokenOre[0];
        
        String endpoint = tokenOre[2].substring(tokenOre[2].indexOf("=") + 1);
        UUID sessionUuid = UUID.randomUUID();
    
        LOGGER.debug("   Session data:");
        LOGGER.debug("      RegToken: {}", regToken);
        LOGGER.debug("      XToken: {}", token);
        LOGGER.debug("      Location: {}", location);
        LOGGER.debug("      Session Id: {}", endpoint);
        LOGGER.debug("      Session UUID: {}", sessionUuid);
    
        return new SkypeSession(regToken, token, location, endpoint, sessionUuid);
    }
    
    public String getXTokenFromUsernamePassword(String username, String password) throws Exception {
        final String hash = hash(username, password);
        
        try {
            Document document = Jsoup.connect("https://api.skype.com/login/skypetoken").data("scopes", "client").data("clientVersion", 
                    SKYPE_VERSION).data("username", username).data("passwordHash", hash).ignoreContentType(true).post();
            final String response = document.body().text();
            
            final JsonObject responseAsJSON = EzSkype.GSON.fromJson(response, JsonObject.class);
            
            return responseAsJSON.get("skypetoken").getAsString();
        } catch (HttpStatusException e) {
            if (e.getStatusCode() == 401) {
                throw new IllegalStateException();
            }
        }
        throw new UnknownError();
    }
    
    public String hash(String username, String password) throws Exception {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            
            byte[] encodedMD = md.digest(String.format(AUTH_PAYLOAD, username, password).getBytes());
            byte[] encodedBase = Base64.encodeBase64(encodedMD);
            
            return new String(encodedBase);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException();
        }
    }
}
