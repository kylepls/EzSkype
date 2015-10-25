package in.kyle.ezskypeezlife.internal.packet;

import in.kyle.ezskypeezlife.EzSkype;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kyle on 10/5/2015.
 */
@ToString
public class WebConnectionBuilder {
    
    // TODO make actual builder
    
    @Setter
    @Getter
    private String url;
    @Setter
    private HTTPRequest request;
    @Getter
    private StringBuilder postData;
    @Getter
    private Map<String, String> headers;
    @Setter
    private ContentType contentType;
    @Setter
    private int timeout = -1;
    @Getter
    private HttpURLConnection connection;
    @Setter
    private boolean showErrors = true;
    
    public WebConnectionBuilder() {
        this.request = HTTPRequest.GET;
        this.postData = new StringBuilder();
        this.headers = new HashMap<>();
        this.contentType = ContentType.WWW_FORM;
    }
    
    /**
     * Sets the location prefix of the remote server
     * (String format %s...)
     * This only for urls that need it
     *
     * @param prefix - The prefix of the server EG: bs1-
     */
    public WebConnectionBuilder locationPrefix(String prefix) {
        this.url = String.format(url, prefix);
        return this;
    }
    
    /**
     * Adds a post parameter to the request
     *
     * @param key   - The key
     * @param value - The value, will be encrypted
     */
    public WebConnectionBuilder addPostData(String key, String value) {
        if (postData.length() != 0) {
            postData.append("&");
        }
        postData.append(key).append("=").append(value);
        return this;
    }
    
    /**
     * Adds a encrypted post parameter to the request
     *
     * @param key   - The key
     * @param value - The value, will be encrypted
     */
    public WebConnectionBuilder addEncodedPostData(String key, String value) {
        try {
            addPostData(key, URLEncoder.encode(value, "UTF-8"));
        } catch (UnsupportedEncodingException ignored) {
        }
        return this;
    }
    
    /**
     * Sets the data to post if the request type is POST
     *
     * @param postData - The data to be posted, must be encrypted first
     */
    public WebConnectionBuilder setPostData(String postData) {
        this.postData = new StringBuilder(postData);
        return this;
    }
    
    /**
     * Adds a non encoded HTTP request header
     *
     * @param key   - The header key
     * @param value - The header value
     */
    public WebConnectionBuilder addHeader(String key, String value) {
        this.headers.put(key, value);
        return this;
    }
    
    /**
     * Executes the web request
     *
     * @return - The string response from the web server
     * @throws IOException
     */
    public String send() throws IOException {
        connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestProperty("Content-Type", contentType.getValue());
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64)");
        connection.setRequestMethod(request.name());
    
        if (timeout != -1) {
            connection.setConnectTimeout(timeout);
        }
        
        headers.forEach(connection::addRequestProperty);
        
        if (request == HTTPRequest.POST || request == HTTPRequest.PUT) {
            connection.setDoOutput(true);
            byte[] data = postData.toString().getBytes();
            connection.addRequestProperty("Content-Length", Integer.toString(data.length));
            connection.getOutputStream().write(data);
            connection.getOutputStream().close();
        }
        
        InputStream inputStream;
        
        try {
            inputStream = connection.getInputStream();
        } catch (Exception e) {
            if (showErrors) {
                if (connection != null && connection.getErrorStream() != null) {
                    StringWriter writer = new StringWriter();
                    IOUtils.copy(connection.getErrorStream(), writer);
                    String string = writer.toString();
                    EzSkype.LOGGER.error("An error occurred while sending data to server\n  Data:\n" + string, e);
                } else {
                    EzSkype.LOGGER.error("An error occurred while sending data to server\n  In = null, Connection: " + connection, e);
                }
            }
            throw e;
        }
        
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            response.append("\n").append(line);
        }
        connection.getInputStream().close();
        return response.toString();
    }
    
    public Document getAsDocument() throws IOException {
        return Jsoup.parse(send());
    }
    
    /**
     * Created by Kyle on 10/5/2015.
     * <p>
     * Sets the Content-Type field of an HTTP request
     */
    @AllArgsConstructor
    public enum ContentType {
        JSON("application/json"), WWW_FORM("application/x-www-form-urlencoded");
        
        @Getter
        private final String value;
    }
    
    /**
     * Sets the request type of an HTTP request
     */
    public enum HTTPRequest {
        GET, POST, PUT, DELETE, OPTIONS
    }
}
