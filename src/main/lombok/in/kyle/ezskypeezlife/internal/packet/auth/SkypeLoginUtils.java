package in.kyle.ezskypeezlife.internal.packet.auth;

import lombok.Cleanup;
import lombok.experimental.UtilityClass;
import org.jsoup.nodes.Document;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Kyle on 11/6/2015.
 */
@UtilityClass
public class SkypeLoginUtils {
    
    public static SkypeJavascriptParams getParameters(Document document) {
        String pie = document.getElementById("pie").val();
        String etm = document.getElementById("etm").val();
        return new SkypeJavascriptParams(pie, etm);
    }
    
    public static String saveHtml(String html) throws IOException {
        String name = "Error-" + new SimpleDateFormat("yyyy-MM-dd-HH-mm").format(new Date()) + ".html";
        File file = new File("logs" + File.separator + "html", name);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        @Cleanup BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        bufferedWriter.append(html);
        return file.getCanonicalPath();
    }
}
