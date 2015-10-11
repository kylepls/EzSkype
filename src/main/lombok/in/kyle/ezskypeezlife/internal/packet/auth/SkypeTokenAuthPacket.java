package in.kyle.ezskypeezlife.internal.packet.auth;

import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;

import java.util.Map;

/**
 * Created by Kyle on 10/6/2015.
 */
public class SkypeTokenAuthPacket extends SkypePacket {
    
    private Map<String, String> cookies;
    
    public SkypeTokenAuthPacket(EzSkype ezSkype, boolean useHeaders, Map<String, String> cookies) {
        super("https://api.asm.skype.com/v1/skypetokenauth", WebConnectionBuilder.HTTPRequest.GET, ezSkype, false);
        this.cookies = cookies;
    }
    
    @Override
    protected SkypeLoginJavascriptParameters run(WebConnectionBuilder webConnectionBuilder) throws Exception {
        // TODO
        return null;
    }
}
