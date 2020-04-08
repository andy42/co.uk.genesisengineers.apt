package resources;

import com.google.common.io.Files;
import util.FileLoader;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

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

    public void copyFile(Path sourceRoot, Path destinationRoot){
        Path source = Paths.get(sourceRoot.toString(), path);
        Path destination = Paths.get(destinationRoot.toString(), path);
        FileLoader.copyFile(source, destination);
    }

    public String createAssetString(){
        String indentation = "      ";
        return  "\n"+indentation+"{\n" +
                indentation+"   \"name\" : \""+createRIdName()+"\",\n"+
                indentation+"   \"id\" : "+id+",\n"+
                indentation+"   \"filePath\" : \""+path+"\",\n"+
                indentation+"   \"type\" : \""+type+"\"\n"+
                indentation+"},";
    }
}
