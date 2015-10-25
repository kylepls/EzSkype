package in.kyle.ezskypeezlife.internal.guest;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import lombok.Getter;
import org.apache.commons.logging.LogFactory;

import java.util.logging.Level;

/**
 * Created by Kyle on 10/23/2015.
 */
public class SkypeWebClient {
    
    @Getter
    private final WebClient webClient;
    
    public SkypeWebClient() {
        webClient = new WebClient(BrowserVersion.CHROME);
        webClient.closeAllWindows();
        webClient.getCache().clear();
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setRedirectEnabled(false);
        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
    }
}
