package in.kyle.ezskypeezlife.internal.packet;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Kyle on 10/5/2015.
 * <p>
 * Sets the Content-Type field of an HTTP request
 */
@AllArgsConstructor
public enum ContentType {
    JSON("application/json"),
    WWW_FORM("application/x-www-form-urlencoded"),
    OCTET_STREAM("application/octet-stream"),
    IMAGE("image/jpeg");
    
    @Getter
    private final String value;
}
