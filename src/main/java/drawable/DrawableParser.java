package drawable;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import referenceFile.ReferenceFileFactory;
import resources.DirectoryNode;
import resources.FileNode;
import util.Logger;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DrawableParser {
    private ReferenceFileFactory referenceFileFactory;
    private DirectoryNode rootDirectoryNode;

    private static final String SHAPE_KEY = "shape";

    public DrawableParser(DirectoryNode directoryNode, ReferenceFileFactory referenceFileFactory){
        this.rootDirectoryNode = directoryNode;
        this.referenceFileFactory = referenceFileFactory;
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
        return true;
    }

    private void parseJsonFile(Path filePath, Path destinationPath ) {
        try {
            InputStream inputStream = Files.newInputStream(filePath);

            String data = IOUtils.toString(inputStream);
            JSONObject jsonObject = new JSONObject(data);

            parseShape(jsonObject);

            BufferedWriter writer = Files.newBufferedWriter(destinationPath, Charset.forName("UTF-8"));
            jsonObject.write(writer);
            writer.write("\n");
            writer.flush();
            writer.close();

        } catch (Exception e) {
            Logger.error(e.getMessage()+" \n"+filePath.toString(), e);
        }
    }

    private void parseShape(JSONObject jsonObject) throws Exception{
        String shapeValue = jsonObject.getString(SHAPE_KEY);
        jsonObject.put(SHAPE_KEY, lookupShapeId(shapeValue));
    }

    private int lookupShapeId(String shapeValue) throws Exception{

        //remove@
        shapeValue = shapeValue.substring(1);

        Integer shapeId = referenceFileFactory.resIdLookUp(shapeValue);
        if(shapeId == null) {
            throw new Exception("shape id not found error  : "+shapeValue);
        }
        return shapeId;
    }
}
