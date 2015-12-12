package in.kyle.ezskypeezlife.api.obj.emoji;

/**
 * Created by Kyle on 12/7/2015.
 */
public interface SkypeMessageElement {
    
    SkypeMessageElementType getType();
    
    String getETag();
    
    String getId();
}
