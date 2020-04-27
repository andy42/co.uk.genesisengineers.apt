package resources;

import assets.Asset;
import assets.AssetsManager;
import com.google.common.io.Files;
import referenceFile.ReferenceFileFactory;
import util.FileLoader;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileNode {
    private int id;
    private File file;
    private String path;
    private String type;
    private String name;
    private String fileType;

    public FileNode(File file, String parentPath, String type, int id){
        this.file = file;
        this.type = type;
        this.fileType = Files.getFileExtension(file.getName());
        this.name = Files.getNameWithoutExtension(file.getName());
        this.id = id;
        this.path = parentPath+"/"+file.getName();
    }

    public static String getFileType(File file){
        String extension = "";

        int i = file.getName().lastIndexOf('.');
        if (i >= 0) {
            extension = file.getName().substring(i+1);
        }
        return extension;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getType () {
        return type;
    }

    public File getFile () {
        return file;
    }

    public String getName () {
        return name;
    }

    public String getFileType () {
        return fileType;
    }

    public void log(){
        System.out.print(type+" : "+name+" : "+fileType+"\n");
    }

    public String createRIdName(){
        return name+ "_"+fileType;
    }

    public String getPathString(){
        return path;
    }

    public Path getPath(Path rootDir){
        return Paths.get(rootDir.toString(),path);
    }

    public void copyFile(Path sourceRoot, Path destinationRoot, AssetsManager assetsManager, ReferenceFileFactory referenceFileFactory){
        Path source = Paths.get(sourceRoot.toString(), path);
        Path destination = Paths.get(destinationRoot.toString(), path);
        FileLoader.copyFile(source, destination);

        addToAssetsManager(assetsManager, referenceFileFactory);
    }

    public void addToAssetsManager(AssetsManager assetsManager, ReferenceFileFactory referenceFileFactory){
        assetsManager.addAsset(
                new Asset.AssetBuilder()
                        .setName(createRIdName())
                        .setId(Integer.toString(id))
                        .setFilePath(path)
                        .setFileType(fileType)
                        .setType(Integer.toString(referenceFileFactory.getId( type, "TYPE")))
                        .build()
        );
    }

    public String createAssetString(ReferenceFileFactory referenceFileFactory){
        String indentation = "      ";
        return  "\n"+indentation+"{\n" +
                indentation+"   \"name\" : \""+createRIdName()+"\",\n"+
                indentation+"   \"id\" : "+id+",\n"+
                indentation+"   \"filePath\" : \""+path+"\",\n"+
                indentation+"   \"fileType\" : \""+fileType+"\",\n"+
                indentation+"   \"type\" : \""+referenceFileFactory.getId( type, "TYPE")+"\"\n"+
                indentation+"},";
    }
}
