package values;

import assets.Asset;
import assets.AssetsManager;
import org.json.JSONArray;
import org.json.JSONObject;
import referenceFile.ReferenceFileFactory;
import util.Logger;

import java.io.BufferedWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ColorResources {

    private ReferenceFileFactory referenceFileFactory;
    private AssetsManager assetsManager;

    private final String COLOR_KEY = "color";
    private final String DEFAULT_KEY = "default";

    private final String COLOR_FILE_NAME = "colors.json";
    private final String COLOR_FILE_ID = "colors_json";

    public ColorResources(ReferenceFileFactory referenceFileFactory, AssetsManager assetsManager){
        this.referenceFileFactory = referenceFileFactory;
        this.assetsManager = assetsManager;
        this.referenceFileFactory.addId("values",COLOR_FILE_ID);
    }

    private Map<String, ColorTheme> themeMap = new HashMap<>();


    public void addColor(String id, String value, String theme){

        referenceFileFactory.addId(COLOR_KEY, id);

        ColorTheme colorTheme = getColorTheme(theme);
        colorTheme.addColor(id, value);
    }

    public void addColor(String id, String value){
        addColor(id, value, DEFAULT_KEY);
    }

    public ColorTheme getColorTheme(String themeName){
        ColorTheme theme = themeMap.get(themeName);
        if(theme == null){
            themeMap.put(themeName, new ColorTheme());
            theme = themeMap.get(themeName);
        }
        return theme;
    }

    private JSONArray createColorData( ){
        JSONArray jsonArray = new JSONArray();
        for(String themeName : themeMap.keySet()){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("theme", themeName);
            jsonObject.put("colors", themeMap.get(themeName).createColorData());
            jsonArray.put(jsonObject);
        }

        return jsonArray;
    }

    public void createColorFile(Path destinationPath ) {

        Path colorPath = Paths.get(destinationPath.toString(), "values", COLOR_FILE_NAME);

        assetsManager.addAsset(
                new Asset.AssetBuilder()
                        .setId(Integer.toString(referenceFileFactory.getId("values", COLOR_FILE_ID)))
                        .setName(COLOR_FILE_ID)
                        .setFilePath("values/"+COLOR_FILE_NAME)
                        .setFileType("json")
                        .setType(Integer.toString(referenceFileFactory.getId("values", "TYPE")))
                        .build()
        );

        JSONArray jsonArray = createColorData();
        try {

            BufferedWriter writer = Files.newBufferedWriter(colorPath, Charset.forName("UTF-8"));
            jsonArray.write(writer);
            writer.write("\n");
            writer.flush();
            writer.close();

        } catch (Exception e) {
            Logger.error(e.getMessage()+" \n"+colorPath.toString(), e);
        }
    }

    private class ColorTheme{
        public Map<String, String> colorMap = new HashMap<>();
        public void addColor(String id, String value){
            colorMap.put(id, value);
        }
        public JSONArray createColorData(){
            JSONArray jsonArray = new JSONArray();
            for(String colorId : colorMap.keySet()){
                JSONObject colorJson = new JSONObject();
                colorJson.put("id", referenceFileFactory.getId(COLOR_KEY,colorId));
                colorJson.put("value", colorMap.get(colorId));
                jsonArray.put(colorJson);
            }
            return jsonArray;
        }
    }
}
