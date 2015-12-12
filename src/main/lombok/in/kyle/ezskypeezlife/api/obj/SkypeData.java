package in.kyle.ezskypeezlife.api.obj;

import in.kyle.ezskypeezlife.internal.obj.SkypeLyncExperiences;
import in.kyle.ezskypeezlife.internal.obj.SkypeTextElementManager;
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
    private SkypeLyncExperiences skypeLyncExperiences;
    private String version;
    private String pesConfigUrl;
    
    private SkypeTextElementManager textElementManager;
    
}
