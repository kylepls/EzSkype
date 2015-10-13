package in.kyle.ezskypeezlife.internal.exception;

import lombok.Data;

/**
 * Created by Kyle on 10/13/2015.
 */
@Data
public class SkypeLoginException extends Exception {
    
    private final String html;
    
}
