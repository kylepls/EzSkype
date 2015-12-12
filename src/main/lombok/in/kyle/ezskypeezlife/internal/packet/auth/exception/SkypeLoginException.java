package in.kyle.ezskypeezlife.internal.packet.auth.exception;

import in.kyle.ezskypeezlife.internal.packet.SkypePacketException;
import in.kyle.ezskypeezlife.internal.packet.auth.SkypeLoginUtils;
import lombok.Getter;

import java.io.IOException;

/**
 * Created by Kyle on 11/6/2015.
 */
public class SkypeLoginException extends SkypePacketException {
    
    @Getter
    private final String html;
    @Getter
    private final String htmlFile;
    
    public SkypeLoginException(String html) throws IOException {
        this.html = html;
        htmlFile = SkypeLoginUtils.saveHtml(html);
    }
    
    @Override
    public String toString() {
        return htmlFile;
    }
}
