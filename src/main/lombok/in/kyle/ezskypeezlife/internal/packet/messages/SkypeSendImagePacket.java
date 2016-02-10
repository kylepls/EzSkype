package in.kyle.ezskypeezlife.internal.packet.messages;

import com.google.gson.JsonObject;
import com.jamesmurty.utils.XMLBuilder;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.internal.packet.ContentType;
import in.kyle.ezskypeezlife.internal.packet.HTTPRequest;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

/**
 * Created by Kyle on 10/7/2015.
 */
public class SkypeSendImagePacket extends SkypePacket {
    
    private final String imageId;
    private final String imageName;
    
    public SkypeSendImagePacket(EzSkype ezSkype, String longId, String imageId, String imageName) {
        super("https://client-s.gateway.messenger.live.com/v1/users/ME/conversations/{}/messages", HTTPRequest.POST, ezSkype, true, longId);
        this.imageId = imageId;
        this.imageName = imageName;
    }
    
    @Override
    protected Object run(WebConnectionBuilder webConnectionBuilder) throws IOException {
    
        long messageId = ezSkype.getMessageId().incrementAndGet();
    
        JsonObject data = new JsonObject();
        String imageLink = "https://api.asm.skype.com/s/i?" + imageId;
    
        // @formatter:off
        String xml = null;
        try {
            xml = XMLBuilder.create("URIObject")
                .a("type", "PicturePoll.1").a("uri", "https://api.asm.skype.com/v1/objects/" + imageId)
                .a("url_thumbnail", "https://api.asm.skype.com/v1/objects/" + imageId + "/views/imgt1")
                    .e("p").t("To view this shared photo, go to: ").up()
                    .e("a").a("href", imageLink).t(imageLink).up()
                    .e("OriginalName").a("v", imageName).up()
                    .e("meta").a("type", "photo").a("originalName", imageName).up().asString();
        } catch (TransformerException | ParserConfigurationException e) {
            e.printStackTrace();
        }
        // @formatter:on
    
        data.addProperty("content", xml);
        data.addProperty("messagetype", "RichText/UriObject");
        data.addProperty("clientmessageid", messageId);
        webConnectionBuilder.setContentType(ContentType.JSON);
    
        webConnectionBuilder.setPostData(data.toString());
        webConnectionBuilder.send();
        return null;
    }
}
