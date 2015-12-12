package in.kyle.ezskypeezlife.api.obj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Kyle on 10/7/2015.
 * <p>
 * Endpoints to be fetched by the poller
 */
@AllArgsConstructor
public enum SkypeEndpoint {
    CONVERSATION_PROPERTIES("/v1/users/ME/conversations/ALL/properties"),
    CONVERSATION_MESSAGES("/v1/users/ME/conversations/ALL/messages"),
    CONTACTS("/v1/users/ME/contacts/ALL"),
    THREADS("/v1/threads/ALL");
    
    @Getter
    private final String urlAppend;
}
