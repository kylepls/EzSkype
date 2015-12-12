package in.kyle.ezskypeezlife.api.obj.emoji;

import lombok.Data;

import java.util.Optional;

/**
 * Created by Kyle on 12/7/2015.
 */
@Data
public class SkypeFlik implements SkypeMessageElement {
    
    private final String eTag;
    private final String title;
    private final Optional<String> auxiliaryText;
    private final Optional<String> auxiliaryUrl;
    private final String transcript;
    private final String copyright;
    private final String id;
    private final boolean sponsored;
    
    @Override
    public SkypeMessageElementType getType() {
        return SkypeMessageElementType.FLIK;
    }
}
