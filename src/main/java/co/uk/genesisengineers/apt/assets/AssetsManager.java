package co.uk.genesisengineers.apt.assets;

import co.uk.genesisengineers.apt.util.Logger;

import java.io.BufferedWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AssetsManager {
    private List<Asset> assets = new ArrayList<>();

    public void addAsset(Asset asset){
        assets.add(asset);
    }

    public void createAssetFile(Path resourcesPath){
        Path assetPath = Paths.get(resourcesPath.toString(), "assets.json");
        try {
            BufferedWriter writer = Files.newBufferedWriter(assetPath, Charset.forName("UTF-8"));
            writer.write(createAssetData());
            writer.flush();
            writer.close();

        } catch (Exception e) {
            Logger.error(e.getMessage()+" \n"+assetPath.toString(), e);
        }
    }

    private String createAssetData(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{\n");
        stringBuilder.append("  \"files\":[\n");

        for(Asset asset : assets){
            stringBuilder.append(asset.createAssetString());
        }
        stringBuilder.deleteCharAt(stringBuilder.length()-1);

        stringBuilder.append("\n  ]\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
