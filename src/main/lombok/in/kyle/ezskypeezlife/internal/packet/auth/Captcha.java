package in.kyle.ezskypeezlife.internal.packet.auth;

import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.api.SkypeCredentials;
import in.kyle.ezskypeezlife.api.captcha.SkypeCaptcha;
import in.kyle.ezskypeezlife.internal.packet.WebConnectionBuilder;
import lombok.Getter;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Copyright (c) 2015 Joe Burnard ("SpongyBacon").
 * By viewing this code, you agree to the terms
 * in the enclosed license.txt file.
 */
public class Captcha {
    
    private EzSkype ezSkype;
    private Document loginDocument;
    private SkypeCredentials credentials;
    private SkypeLoginJavascriptParameters parameters;
    
    private BufferedReader in;
    
    @Getter
    private boolean solved = false;
    
    public Captcha(EzSkype ezSkype, SkypeCredentials credentials, SkypeLoginJavascriptParameters parameters, Document loginDocument) {
        this.ezSkype = ezSkype;
        this.credentials = credentials;
        this.parameters = parameters;
        this.loginDocument = loginDocument;
        this.in = new BufferedReader(new InputStreamReader(System.in));
    }
    
    public void setSolved(boolean solved) {
        this.solved = solved;
        
        try {
            in.close(); // close because input it no longer needed
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private String getImageUrl(String html) throws IOException {
        String rawUrl = html.split("var skypeHipUrl = \"")[1].split("\";")[0];

        /*
        String jsText = Jsoup.connect(rawUrl)
                .header("User-Agent", "Mozilla/5.0")
                .ignoreContentType(true)
                .method(Connection.Method.GET)
                .get()
                .text();
         */
        
        WebConnectionBuilder webConnectionBuilder = new WebConnectionBuilder();
        webConnectionBuilder.setUrl(rawUrl);
        String jsText = webConnectionBuilder.send();
        
        return jsText.split("imageurl:'")[1].split("',")[0];
    }
    
    private String getSystemInput() throws IOException {
        return in.readLine();
    }
    
    private String getToken(String image) {
        return image.split("hid=")[1].split("&fid")[0];
    }
    
    private String getFID(String image) {
        return image.split("fid=")[1].split("&id")[0];
    }
    
    public String requestSolution() throws Exception {
        String url = null;
        
        try {
            url = getImageUrl(loginDocument.body().html());
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        if (url == null) {
            System.out.println("Could not find captcha image!");
            System.out.println(loginDocument.body().html());
            throw new RuntimeException();
        }
        
        System.out.println("Solution for " + url + " ?");
        
        String solution = null;
        try {
            solution = getSystemInput();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        String token = getToken(url);
        String fid = getFID(url);
        
        SkypeCaptcha skypeCaptcha = new SkypeCaptcha(token, fid, url);
        skypeCaptcha.setSolution(solution);
        try {
            
            SkypeLoginPacket loginPacket = new SkypeLoginPacket(ezSkype, credentials, parameters, skypeCaptcha);
            String value = (String) loginPacket.executeSync();
            setSolved(true);
            System.out.println("Solved captcha!");
            return value;
        } catch (Exception e) {
            System.out.println("Could not solve captcha.");
            throw e;
        }
    }
}