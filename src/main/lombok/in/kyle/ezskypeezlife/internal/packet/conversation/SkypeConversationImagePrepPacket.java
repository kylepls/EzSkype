package in.kyle.ezskypeezlife.internal.packet.conversation;

import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;

/**
 * Created by Kyle on 10/30/2015.
 */
public class SkypeConversationImagePrepPacket extends SkypePacket {
    
    public SkypeConversationImagePrepPacket(EzSkype ezSkype, String longId) {
        super("https://client-s.gateway.messenger.live.com/v1/users/ME/conversations/" + longId + "/messages", WebConnectionBuilder
                .HTTPRequest.POST, ezSkype, true);
    }
    
    @Override
    protected Object run(WebConnectionBuilder webConnectionBuilder) throws Exception {
        
        
        //String data = "{content: \"<URIObject type=\\\"PicturePoll.1\\\" uri=\\\"https://api.asm.skype.com/v1/objects/" + ids + "\\\" " +
        //        "url_thumbnail=\\\"https://api.asm.skype.com/v1/objects/"+ ids + "/views/imgt1\\\">To view this shared photo, go to: <a
        // href=\\\"https://api.asm.skype.com/s/i?" + ids + "\\\">https://api.asm.skype.com/s/i?" + ids + "<\\/a><OriginalName 
        // v=\\\"^005CFF2010F86CC63570CA528D9B2CCFE3BF3B54DF8A01E92E^pimgpsh_thumbnail_win_distr.jpg\\\"/><meta type=\\\"photo\\\" 
        // originalName=\\\"^005CFF2010F86CC63570CA528D9B2CCFE3BF3B54DF8A01E92E^pimgpsh_thumbnail_win_distr.jpg\\\"/><\\/URIObject>\", 
        // messagetype: \"RichText/UriObject\", contenttype: \"text\", clientmessageid: \"" + id + "\"}";
        
        
        return null;
    }
}
