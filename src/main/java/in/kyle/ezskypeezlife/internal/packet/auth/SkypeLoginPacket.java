package in.kyle.ezskypeezlife.internal.packet.auth;

import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.api.SkypeCredentials;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Kyle on 10/5/2015.
 */
public class SkypeLoginPacket extends SkypePacket {
    
    private final SkypeCredentials skypeCredentials;
    private final SkypeLoginJavascriptParameters javascriptParameters;
    
    public SkypeLoginPacket(EzSkype ezSkype, SkypeCredentials credentials, SkypeLoginJavascriptParameters javascriptParameters) {
        super("https://login.skype.com/login?client_id=578134&redirect_uri=https%3A%2F%2Fweb.skype.com", WebConnectionBuilder.HTTPRequest
                .POST, ezSkype, false);
        this.skypeCredentials = credentials;
        this.javascriptParameters = javascriptParameters;
    }
    
    @Override
    protected String run(WebConnectionBuilder webConnectionBuilder) throws 
            IOException {
        Date date = new Date();
        webConnectionBuilder.setRequest(WebConnectionBuilder.HTTPRequest.POST);
        webConnectionBuilder.addEncodedPostData("username", skypeCredentials.getUsername());
        webConnectionBuilder.addEncodedPostData("password", new String(skypeCredentials.getPassword()));
        webConnectionBuilder.addEncodedPostData("timezone_field", new SimpleDateFormat("XXX").format(date).replace(':', '|'));
        webConnectionBuilder.addEncodedPostData("pie", javascriptParameters.getPie());
        webConnectionBuilder.addEncodedPostData("etm", javascriptParameters.getEtm());
        webConnectionBuilder.addEncodedPostData("js_time", Long.toString(date.getTime() / 1000));
        webConnectionBuilder.addEncodedPostData("redirect_uri", "https://web.skype.com");
        
        Document document = webConnectionBuilder.getAsDocument();
        Elements elementsByAttributeValue = document.getElementsByAttributeValue("name", "skypetoken");
    
        String token = elementsByAttributeValue.get(0).val();
        return token;
    }
}
