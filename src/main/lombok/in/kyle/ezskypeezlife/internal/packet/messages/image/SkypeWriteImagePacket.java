package in.kyle.ezskypeezlife.internal.packet.messages.image;

import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.internal.packet.ContentType;
import in.kyle.ezskypeezlife.internal.packet.HTTPRequest;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Kyle on 11/3/2015.
 */
public class SkypeWriteImagePacket extends SkypePacket {
    
    private final InputStream inputStream;
    
    public SkypeWriteImagePacket(EzSkype ezSkype, String imageId, InputStream inputStream) {
        super("https://api.asm.skype.com/v1/objects/" + imageId + "/content/imgpsh", HTTPRequest.PUT, ezSkype, false);
        this.inputStream = inputStream;
    }
    
    @Override
    protected Object run(WebConnectionBuilder webConnectionBuilder) throws IOException {
    
        webConnectionBuilder.setContentType(ContentType.OCTET_STREAM);
        webConnectionBuilder.addHeader("Authorization", "skype_token " + ezSkype.getSkypeSession().getXToken());
        webConnectionBuilder.setWriteStream(inputStream);
        
        webConnectionBuilder.send();
        return null;
    }
}
