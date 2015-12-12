package in.kyle.ezskypeezlife.internal.obj;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.api.obj.emoji.SkypeEmoji;
import in.kyle.ezskypeezlife.api.obj.emoji.SkypeFlik;
import in.kyle.ezskypeezlife.api.obj.emoji.SkypeMessageElement;
import in.kyle.ezskypeezlife.api.obj.emoji.SkypeMessageElementType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by Kyle on 12/7/2015.
 */
@Data
public class SkypeTextElementManager {
    
    private final List<SkypeMessageElement> all;
    
    private final List<SkypeEmoji> emojis;
    private final List<SkypeFlik> fliks;
    
    public SkypeTextElementManager(JsonObject textElements) {
        emojis = new ArrayList<>();
        fliks = new ArrayList<>();
        all = new ArrayList<>();
        loadFliks(textElements);
    }
    
    private void loadFliks(JsonObject textElements) {
        JsonArray items = textElements.getAsJsonArray("items");
        for (JsonElement jsonElement : items) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            
            String typeName = jsonObject.get("type").getAsString().toUpperCase();
            
            SkypeMessageElementType type = SkypeMessageElementType.valueOf(typeName);
            
            String eTag = jsonObject.get("etag").getAsString();
            String id = jsonObject.get("id").getAsString();
            
            if (type == SkypeMessageElementType.EMOTICON) {
                String description = jsonObject.get("description").getAsString();
                JsonArray shortcutsArray = jsonObject.getAsJsonArray("shortcuts");
                List<String> shortcuts = StreamSupport.stream(shortcutsArray.spliterator(), false).map(JsonElement::getAsString).collect
                        (Collectors.toList());
                SkypeEmoji skypeEmoji = new SkypeEmoji(eTag, id, description, shortcuts);
                emojis.add(skypeEmoji);
            } else {
                String title = jsonObject.get("pickerTitle").getAsString();
                Optional<String> auxiliaryText = !jsonObject.get("auxiliaryText").isJsonNull() ? Optional.of(jsonObject.get
                        ("auxiliaryText").getAsString()) : Optional.empty();
                Optional<String> auxiliaryUrl = !jsonObject.get("auxiliaryUrl").isJsonNull() ? Optional.of(jsonObject.get("auxiliaryUrl")
                        .getAsString()) : Optional.empty();
                String transcript = jsonObject.get("transcript").getAsString();
                String copyright = jsonObject.get("copyright").getAsString();
                boolean sponsored = jsonObject.get("isSponsored").getAsBoolean();
                SkypeFlik skypeFlik = new SkypeFlik(eTag, title, auxiliaryText, auxiliaryUrl, transcript, copyright, id, sponsored);
                fliks.add(skypeFlik);
            }
        }
        all.addAll(fliks);
        all.addAll(emojis);
    }
    
    public Optional<SkypeEmoji> getEmoji(String shortcut) {
        return emojis.stream().filter(skypeEmoji -> skypeEmoji.getShortcuts().contains(shortcut)).findAny();
    }
    
    public Optional<SkypeMessageElement> getElement(String id) {
        return all.stream().filter(skypeMessageElement -> skypeMessageElement.getId().equals(id)).findAny();
    }
}
