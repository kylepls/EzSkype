package in.kyle.ezskypeezlife.internal.thread;

import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.exception.SkypeException;

/**
 * Created by Kyle on 12/7/2015.
 */
public class SkypeParseException extends SkypeException {
    
    public SkypeParseException(JsonObject jsonObject, String message) {
        super(message + "\nJsonObject: " + jsonObject);
    }
}
