package in.kyle.ezskypeezlife.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Kyle on 10/8/2015.
 */
@AllArgsConstructor
public enum SkypeGender {
    
    // TODO tbh I made up these numbers
    MALE(1), FEMALE(2), OTHER(3), UNKNOWN(4);
    
    @Getter
    private final int skypeValue;
    
    public static SkypeGender getGenderFromSkypeValue(int value) {
        for (SkypeGender skypeGender : values()) {
            if (skypeGender.getSkypeValue() == value) {
                return skypeGender;
            }
        }
        return SkypeGender.UNKNOWN;
    }
}
