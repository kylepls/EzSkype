package in.kyle.ezskypeezlife.internal.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SkypeException extends Exception {
    private final String html;
}
