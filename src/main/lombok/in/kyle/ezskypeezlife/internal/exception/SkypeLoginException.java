package in.kyle.ezskypeezlife.internal.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SkypeLoginException extends SkypeException {

    private final String html;

}
