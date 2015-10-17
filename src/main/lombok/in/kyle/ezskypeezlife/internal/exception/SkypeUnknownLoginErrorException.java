package in.kyle.ezskypeezlife.internal.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by Kyle on 10/13/2015.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SkypeUnknownLoginErrorException extends Exception {
    
    private final String html;
}
