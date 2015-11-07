package in.kyle.ezskypeezlife.internal.packet;

import in.kyle.ezskypeezlife.EzSkype;
import lombok.ToString;

import java.util.concurrent.Future;

/**
 * Created by Kyle on 10/5/2015.
 */
@ToString
public abstract class SkypePacket {
    
    protected EzSkype ezSkype;
    private String url;
    private HTTPRequest httpRequest;
    private boolean useHeaders;
    
    public SkypePacket(String url, HTTPRequest httpRequest, EzSkype ezSkype, boolean useHeaders) {
        this.url = url;
        this.httpRequest = httpRequest;
        this.ezSkype = ezSkype;
        this.useHeaders = useHeaders;
    }
    
    /**
     * Executes the request
     *
     * @throws Exception
     */
    public Future<Object> executeAsync() {
        return ezSkype.getPacketIOPool().sendPacket(this);
    }
    
    public Object executeSync() throws Exception {
        WebConnectionBuilder webConnectionBuilder = getConnectionBuilder();
        EzSkype.LOGGER.debug("Opening connection: " + this.getClass().getCanonicalName() + " " + this + "\n    Connection: " + 
                webConnectionBuilder);
        return run(webConnectionBuilder);
    }
    
    private WebConnectionBuilder getConnectionBuilder() {
        WebConnectionBuilder builder = new WebConnectionBuilder();
        if (ezSkype.getProxy() != null) {
            builder.setProxy(ezSkype.getProxy());
        }
        builder.setUrl(url);
        builder.setRequest(httpRequest);
        if (useHeaders) {
            builder.addHeaders(ezSkype);
        }
        return builder;
    }
    
    /**
     * Method called when request is executed
     * The URL, request type, and headers are already set
     * <p>
     * This method should call the send() method in the WebConnectionBuilder class
     *
     * @param webConnectionBuilder - The connection builder
     * @return The data returned from the packet
     * @throws Exception
     */
    protected abstract Object run(WebConnectionBuilder webConnectionBuilder) throws Exception;
}
