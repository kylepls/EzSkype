package in.kyle.ezskypeezlife.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * Created by Kyle on 10/6/2015.
 */
@AllArgsConstructor
public enum SkypeMessageType {
    
    TEXT("Text"),
    RICH_TEXT("RichText"),
    MEDIA_FLIKMSG("RICHTEXT/MEDIA_FLIKMSG");
    
    @Getter
    private final String name;
    
    public static SkypeMessageType getType(String name) {
        return Stream.of(values()).filter(skypeMessageType -> skypeMessageType.getName().equalsIgnoreCase(name)).findAny().orElse(null);
    }
}
