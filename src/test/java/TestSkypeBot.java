import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.api.SkypeCredentials;
import in.kyle.ezskypeezlife.api.obj.SkypeMessage;
import in.kyle.ezskypeezlife.events.conversation.*;
import in.kyle.ezskypeezlife.events.user.SkypeContactAddedEvent;

import java.io.File;
import java.io.FileReader;

/**
 * Created by Kyle on 10/8/2015.
 */
public class TestSkypeBot {
    
    public static void main(String[] args) throws Exception {
        new TestSkypeBot().startTest();
    }
    
    public void startTest() throws Exception {
        System.out.println("Logging in");
        
        // Load credentials from a file 'login.json'
        LoginCredentials loginCredentials = EzSkype.GSON.fromJson(new FileReader(new File("login.json")), LoginCredentials.class);
        
        // Enter the Skype login info here and login
        EzSkype ezSkype = new EzSkype(new SkypeCredentials(loginCredentials.getUser(), loginCredentials.getPass())).login();
        
        // Register all the events in this class
        // Events are denoted as methods that have 1 parameter that implements SkypeEvent
        ezSkype.getEventManager().registerEvents(this);
    }
    
    // Called when a new message is received from Skype
    public void onMessage(SkypeMessageReceivedEvent e) {
        String message = e.getMessage().getMessage(); // Get the message content
        System.out.println("Got message: " + e.getMessage().getSender().getUsername() + " - " + message);
        
        switch (message) {
            case "+ping":  // Replies to a conversation with the message pong
                e.reply("Pong!");
                break;
            case "+me":  // Prints out user information
                e.reply("You: " + e.getMessage().getSender());
                break;
            case "+edit":  // Tests message editing
                SkypeMessage sendMessage = e.reply("Testing message " + "editing...");
                sendMessage.edit("Message editing works!");
                e.reply("Message edit test complete");
                break;
            case "+contact":  // Adds the sender as a contact
                e.getMessage().getSender().setContact(true);
                e.reply("Sent");
                break;
        }
    }
    
    // Called when the topic of a conversation changes
    public void onTopic(SkypeConversationUpdateTopicEvent e) {
        e.getConversation().sendMessage("Topic changed\nFrom: " + e.getOldTopic() + "\nNew: " + e.getNewTopic());
    }
    
    // Called when a user joins a conversation
    public void onUserJoin(SkypeConversationUserJoinEvent e) {
        e.getConversation().sendMessage("User joined: " + e.getUser().getUsername());
    }
    
    // Called when a user leaves a conversation
    public void onUserLeave(SkypeConversationUserLeaveEvent e) {
        e.getConversation().sendMessage("User left: " + e.getUser().getUsername());
    }
    
    // Called when a user changes role in a conversation
    public void onRole(SkypeConversationUserRoleUpdate e) {
        e.getConversation().sendMessage("Role update: " + e.getUser().getUsername() + "\nFrom: " + e.getOldRole() +
                "\nTo: " + e.getNewRole());
    }
    
    // Called when a call is initiated by a user
    public void onCallStart(SkypeConversationCallStartedEvent e) {
        e.getConversation().sendMessage("Call started by " + e.getUser().getUsername());
    }
    
    // Called when a call is ended
    public void onCallEnd(SkypeConversationCallEndedEvent e) {
        e.getConversation().sendMessage("Call ended by " + e.getUser().getUsername());
    }
    
    // Called when the bot is added to a conversation
    public void onAdd(SkypeConversationAddedToEvent e) {
        e.getConversation().sendMessage("Thanks for adding me to this conversation!");
    }
    
    // Called when the bot is removed from a conversation
    public void onRemove(SkypeConversationKickedFromEvent e) {
        System.out.println("I got removed from a conversation by " + e.getUser().getUsername());
    }
    
    // Called when a conversation picture is changed
    public void onPicture(SkypeConversationPictureChangeEvent e) {
        e.getConversation().sendMessage("Conversation picture changed\nFrom: " + e.getOldPicture() + "\nTo: " + e.getNewPicture());
    }
    
    // Called when a contact is added
    public void onContact(SkypeContactAddedEvent e) {
        System.out.println("Got contact " + e.getUser().getUsername());
    }
}
