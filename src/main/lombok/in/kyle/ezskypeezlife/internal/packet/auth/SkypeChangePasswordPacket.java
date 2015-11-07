package in.kyle.ezskypeezlife.internal.packet.auth;

import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.internal.packet.HTTPRequest;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;

import java.util.Date;

/**
 * Created by Kyle on 11/6/2015.
 */
public class SkypeChangePasswordPacket extends SkypePacket {
    
    
    private final SkypeJavascriptParams params;
    private final String username;
    private final String passwordNew;
    
    public SkypeChangePasswordPacket(EzSkype ezSkype, SkypeJavascriptParams params, String username, String passwordNew) {
        super("https://login.skype.com/recovery/password-change?client_id=578134&redirect_uri=https%3A%2F%2Fweb.skype.com", HTTPRequest
                .POST, ezSkype, false);
        this.params = params;
        this.username = username;
        this.passwordNew = passwordNew;
    }
    
    @Override
    protected String run(WebConnectionBuilder webConnectionBuilder) throws Exception {
        
        Date date = new Date();
        webConnectionBuilder.addPostData("username", username);
        webConnectionBuilder.addPostData("prefill", Integer.toString(1));
        webConnectionBuilder.addPostData("password_new", passwordNew);
        webConnectionBuilder.addPostData("repeat_password_new", passwordNew);
        webConnectionBuilder.addPostData("pie", params.getPie());
        webConnectionBuilder.addPostData("etm", params.getEtm());
        webConnectionBuilder.addEncodedPostData("js_time", Long.toString(date.getTime() / 1000));
        webConnectionBuilder.addEncodedPostData("redirect_uri", "https://web.skype.com");
        
        return webConnectionBuilder.send();
    }
}
