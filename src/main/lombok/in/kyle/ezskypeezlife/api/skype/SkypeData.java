package in.kyle.ezskypeezlife.api.skype;

import in.kyle.ezskypeezlife.api.errors.SkypeTextElementManager;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jsoup.nodes.Document;

/**
 * Created by Kyle on 12/7/2015.
 */
@Data
@AllArgsConstructor
public class SkypeData {
    
    private Document page;
    private String mainJs;
    private String bootStrapJs;
    private String version;
    private String pesConfigUrl;
    private SkypeTextElementManager textElementManager;
    
}
