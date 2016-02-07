package in.kyle.ezskypeezlife.internal.packet;

import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.exception.SkypeException;
import lombok.Getter;
import lombok.ToString;

import java.io.IOException;
import java.util.concurrent.Future;

/**
 * Created by Kyle on 10/5/2015.
 */
@ToString
public abstract class SkypePacket {
    
    @Getter
    protected final EzSkype ezSkype;
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
     * Executes the request on the PacketIO thread
     *
     * @return - The object returned by the packet
     */
    public Future<Object> executeAsync() {
        return ezSkype.getPacketIOPool().sendPacket(this);
    }
    
    public Object executeSync() throws SkypeException, IOException {
        WebConnectionBuilder webConnectionBuilder = getConnectionBuilder();
        EzSkype.LOGGER.debug("Opening connection: " + this.getClass().getCanonicalName() + " " + this + "\n    Connection: " +
                webConnectionBuilder);
        return run(webConnectionBuilder);
    }
    
    private WebConnectionBuilder getConnectionBuilder() {
        WebConnectionBuilder builder = new WebConnectionBuilder();
        if (ezSkype != null && ezSkype.getSkypeProperties().getProxy().isPresent()) {
            builder.setProxy(ezSkype.getSkypeProperties().getProxy().get());
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
     * @throws SkypeException - If an error occurred while parsing the request or some other non IO related error
     * @throws IOException    - If there was an error sending data to the server
     */
    protected abstract Object run(WebConnectionBuilder webConnectionBuilder) throws SkypeException, IOException;
}
