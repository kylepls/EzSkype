package in.kyle.ezskypeezlife.api.obj.emoji;

import lombok.Data;

import java.util.List;

/**
 * Created by Kyle on 12/7/2015.
 */
@Data
public class SkypeEmoji implements SkypeMessageElement {
    
    private final String eTag;
    private final String id;
    private final String description;
    private final List<String> shortcuts;
    
    @Override
    public SkypeMessageElementType getType() {
        return SkypeMessageElementType.EMOTICON;
    }
}
