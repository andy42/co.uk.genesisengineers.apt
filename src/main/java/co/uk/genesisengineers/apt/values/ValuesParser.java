package co.uk.genesisengineers.apt.values;

import co.uk.genesisengineers.apt.assets.AssetsManager;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import co.uk.genesisengineers.apt.referenceFile.ReferenceFileFactory;
import co.uk.genesisengineers.apt.resources.DirectoryNode;
import co.uk.genesisengineers.apt.resources.FileNode;
import co.uk.genesisengineers.apt.util.Logger;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ValuesParser {
    private ReferenceFileFactory referenceFileFactory;
    private DirectoryNode rootDirectoryNode;

    private ColorResources colorResources;

    private final String ID_KEY = "id";
    private final String VALUE_KEY = "value";
    private final String VALUE_NAME = "name";
    private final String TYPE_KEY = "type";
    private final String THEME_KEY = "theme";

    private final String TYPE_COLOR_KEY = "color";
    private final String TYPE_COLOuR_KEY = "colour";
    private final String TYPE_ID = "id";
    private final String TYPE_COMPONENT = "component";


    public ValuesParser(DirectoryNode directoryNode, ReferenceFileFactory referenceFileFactory, AssetsManager assetsManager){
        this.rootDirectoryNode = directoryNode;
        this.referenceFileFactory = referenceFileFactory;
        this.colorResources = new ColorResources(referenceFileFactory, assetsManager);
    }

    public boolean parse(Path sourceRoot, Path destinationRoot ){
        rootDirectoryNode.createDirAtRootDestination(destinationRoot);

        for(FileNode fileNode : rootDirectoryNode.getFileList()){
            switch (fileNode.getFileType()){
                case "json":
                    parseJsonFile(
                            fileNode.getPath(sourceRoot),
                            Paths.get(destinationRoot.toString(), fileNode.getPathString()));
                    break;
            }
        }

        colorResources.createColorFile(destinationRoot);
        return true;
    }

    private void parseJsonFile(Path filePath, Path destinationPath ) {
        try {
            InputStream inputStream = Files.newInputStream(filePath);

            String data = IOUtils.toString(inputStream);
            JSONArray jsonArray = new JSONArray(data);
            for(int i=0; i< jsonArray.length(); i++){
                parseValueObject(jsonArray.getJSONObject(i));
            }

//            BufferedWriter writer = Files.newBufferedWriter(destinationPath, Charset.forName("UTF-8"));
//            jsonArray.write(writer);
//            writer.write("\n");
//            writer.flush();
//            writer.close();

        } catch (Exception e) {
            Logger.error(e.getMessage()+" \n"+filePath.toString(), e);
        }
    }

    private void parseValueObject(JSONObject jsonObject){
        String type = jsonObject.getString(TYPE_KEY);
        switch (type){
            case TYPE_COLOR_KEY:
            case TYPE_COLOuR_KEY:
                parseColor(jsonObject);
                break;
            case TYPE_ID:
                parseId(jsonObject);
                break;
            case TYPE_COMPONENT:
                parseComponent(jsonObject);
                break;
        }

    }

    private void parseComponent(JSONObject jsonObject){
        String idName = jsonObject.getString(VALUE_NAME);
        referenceFileFactory.addId(TYPE_COMPONENT, idName);
    }

    private void parseId(JSONObject jsonObject){
        String idName = jsonObject.getString(VALUE_NAME);
        referenceFileFactory.addId(ID_KEY, idName);
    }

    private void parseColor(JSONObject jsonObject){
        String id = jsonObject.getString(ID_KEY);
        String value = jsonObject.getString(VALUE_KEY);
        String theme = null;

        try{
            theme = jsonObject.getString(THEME_KEY);
        }
        catch (Exception e){ }

        if(theme == null) {
            colorResources.addColor(id, value);
        } else {
            colorResources.addColor(id, value, theme);
        }
    }
}
