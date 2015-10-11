package in.kyle.ezskypeezlife.internal.packet.user;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.api.SkypeGender;
import in.kyle.ezskypeezlife.internal.obj.SkypeLocalUserInternal;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Kyle on 10/8/2015.
 */
public class SkypeGetSelfInfoPacket extends SkypePacket {
    
    public SkypeGetSelfInfoPacket(EzSkype ezSkype) {
        super("https://api.skype.com/users/self/profile", WebConnectionBuilder.HTTPRequest.GET, ezSkype, true);
    }
    
    @Override
    protected SkypeLocalUserInternal run(WebConnectionBuilder webConnectionBuilder) throws Exception {
        JsonObject response = EzSkype.GSON.fromJson(webConnectionBuilder.send(), JsonObject.class);
        
        String username = response.get("username").getAsString();
        
        SkypeLocalUserInternal localUserInternal = new SkypeLocalUserInternal(username, ezSkype);
        
        JsonElement about = response.get("about");
        JsonElement lastName = response.get("lastname");
        JsonElement birthday = response.get("birthday");
        JsonElement gender = response.get("gender");
        JsonElement country = response.get("country");
        JsonElement city = response.get("city");
        JsonElement language = response.get("language");
        JsonElement homepage = response.get("homepage");
        JsonElement province = response.get("province");
        JsonElement jobTitle = response.get("jobtitle");
        JsonElement emails = response.get("emails");
        JsonElement phoneMobile = response.get("phoneMobile");
        JsonElement phoneHome = response.get("phoneHome");
        JsonElement phoneOffice = response.get("phoneOffice");
        JsonElement mood = response.get("mood");
        JsonElement richMood = response.get("richMood");
        JsonElement avatarUrl = response.get("avatarUrl");
        
        if (!about.isJsonNull()) {
            localUserInternal.setAbout(Optional.of(about.getAsString()));
        }
        //if (!lastName.isJsonNull()) {
        //    localUserInternal.setLastName(Optional.of(lastName.getAsString()));
        //}
        if (!birthday.isJsonNull()) {
            localUserInternal.setBirthday(Optional.of(birthday.getAsString()));
        }
        if (!gender.isJsonNull()) {
            localUserInternal.setGender(Optional.of(SkypeGender.getGenderFromSkypeValue(gender.getAsInt())));
        }
        if (!country.isJsonNull()) {
            localUserInternal.setCountry(Optional.of(country.getAsString()));
        }
        if (!city.isJsonNull()) {
            localUserInternal.setCity(Optional.of(city.getAsString()));
        }
        if (!language.isJsonNull()) {
            localUserInternal.setLanguage(Optional.of(language.getAsString()));
        }
        if (!homepage.isJsonNull()) {
            localUserInternal.setHomepage(Optional.of(homepage.getAsString()));
        }
        if (!province.isJsonNull()) {
            localUserInternal.setProvince(Optional.of(province.getAsString()));
        }
        if (!jobTitle.isJsonNull()) {
            localUserInternal.setJobTitle(Optional.of(jobTitle.getAsString()));
        }
        if (!emails.isJsonNull()) {
            JsonArray emailsJson = response.getAsJsonArray("emails");
            List<String> emailList = new ArrayList<>();
            emailsJson.forEach(email -> emailList.add(email.getAsString()));
            localUserInternal.setEmails(emailList);
        }
        if (!phoneMobile.isJsonNull()) {
            localUserInternal.setPhoneMobile(Optional.of(phoneMobile.getAsString()));
        }
        if (!phoneHome.isJsonNull()) {
            localUserInternal.setPhoneHome(Optional.of(phoneHome.getAsString()));
        }
        if (!phoneOffice.isJsonNull()) {
            localUserInternal.setPhoneOffice(Optional.of(phoneOffice.getAsString()));
        }
        if (!mood.isJsonNull()) {
            localUserInternal.setMood(Optional.of(mood.getAsString()));
        }
        if (!richMood.isJsonNull()) {
            localUserInternal.setRichMood(Optional.of(richMood.getAsString()));
        }
        if (!avatarUrl.isJsonNull()) {
            localUserInternal.setAvatarUrl(Optional.of(avatarUrl.getAsString()));
        }
        
        return localUserInternal;
    }
}
