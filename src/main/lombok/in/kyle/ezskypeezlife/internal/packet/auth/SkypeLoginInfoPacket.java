package in.kyle.ezskypeezlife.internal.packet.auth;

import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.internal.packet.HTTPRequest;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;
import org.jsoup.nodes.Document;

/**
 * Created by Kyle on 10/5/2015.
 */
public class SkypeLoginInfoPacket extends SkypePacket {
    
    public SkypeLoginInfoPacket(EzSkype ezSkype) {
        super("https://login.skype.com/login?client_id=578134&redirect_uri=https%3A%2F%2Fweb.skype.com", HTTPRequest
                .GET, ezSkype, false);
    }
    
    @Override
    protected SkypeJavascriptParams run(WebConnectionBuilder webConnectionBuilder) throws Exception {
        Document document = webConnectionBuilder.getAsDocument();
        return SkypeLoginUtils.getParameters(document);
    }
    
}
