package in.kyle.ezskypeezlife.internal.packet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.slf4j.Logger;

import java.util.List;

/**
 * Created by Kyle on 2/9/2016.
 */
@AllArgsConstructor
public class PacketLogger {
    
    private final Logger logger;
    private final List<Message> messages;
    
    public void debug(String string, String... args) {
        messages.add(new Message(MessageType.ERROR, string, args));
    }
    
    public void info(String string, String... args) {
        messages.add(new Message(MessageType.ERROR, string, args));
    }
    
    public void error(String string, Throwable throwable) {
        messages.add(new Message(MessageType.ERROR, string, throwable));
    }
    
    public void flush() {
        messages.forEach(message -> {
            switch (message.getMessageType()) {
                case DEBUG:
                    logger.debug(message.getKey(), message.getArgs());
                    break;
                case INFO:
                    logger.info(message.getKey(), message.getArgs());
                    break;
                case ERROR:
                    logger.error(message.getKey(), (Throwable) message.getArgs());
                    break;
            }
        });
    }
    
    @AllArgsConstructor
    @Getter
    private enum MessageType {
        DEBUG, INFO, ERROR
    }
    
    @Data
    private class Message {
        
        private final MessageType messageType;
        private final String key;
        private final Object args;
        
    }
}
