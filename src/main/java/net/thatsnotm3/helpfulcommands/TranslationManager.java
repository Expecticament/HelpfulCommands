package net.thatsnotm3.helpfulcommands;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.LanguageManager;

import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.nio.charset.StandardCharsets;

public class TranslationManager{
    
    public static String getTranslation(String key){
        LanguageManager languageManager=MinecraftClient.getInstance().getLanguageManager();
        String currentLanguage=languageManager.getLanguage();
        String filePath="/assets/"+HelpfulCommands.modID+"/lang/"+currentLanguage+".json";
        String defaultLangFilePath="/assets/"+HelpfulCommands.modID+"/lang/en_us.json";
        
        String translation=null;
        translation=getTranslationInFile(filePath, key);
        if(translation==null) translation=getTranslationInFile(defaultLangFilePath, key);
        if(translation==null) translation=key;
        
        return translation;
    }
    static String getTranslationInFile(String filePath,String key){
        String ret=null;
        try{
            InputStream inputStream=HelpfulCommands.class.getResourceAsStream(filePath);
            InputStreamReader reader=new InputStreamReader(inputStream,StandardCharsets.UTF_8);
            JsonElement jsonElement=JsonParser.parseReader(reader);
            JsonObject json=jsonElement.getAsJsonObject();
            ret=json.get(key).getAsString();
        } catch(Exception e){
            HelpfulCommands.LOGGER.error("Failed to get translation", e);
        }
        return ret;
    }
}