package in.kyle.ezskypeezlife.internal.packet.conversation;

public class SkypeConversationGetJoinUrl extends SkypePacket {
    
    private final String longId;
    
    public SkypeConversationGetJoinUrl(EzSkype ezSkype, String longId) {
        super("https://api.scheduler.skype.com/threads", HTTPRequest.POST, ezSkype, true);
        this.longId = longId;
    }
    
    @Override
    protected Object run(WebConnectionBuilder webConnectionBuilder) throws IOException {
        JsonObject data = new JsonObject();
        data.addProperty("baseDomain", "https://join.skype.com/launch/");
        data.addProperty("threadId", longId);
        
        webConnectionBuilder.setPostData(data.toString());
        
        JsonObject response = EzSkype.GSON.fromJson(webConnectionBuilder.send(), JsonObject.class);
        
        return response.get("JoinUrl").getAsString();
    }
}