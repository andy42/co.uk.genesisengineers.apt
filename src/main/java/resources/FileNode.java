package resources;

import com.google.common.io.Files;

import java.io.File;

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
        //System.out.print(type+": "+file.getAbsolutePath()+"\n");
        System.out.print(type+" : "+name+" : "+fileType+"\n");
    }

    private String createName(boolean prefix_file_type){
        return name+((prefix_file_type) ? "_"+fileType : "");
    }

    public String createResourceString(boolean prefix_file_type){
        return "public static final int "+createName(prefix_file_type)+" = "+id+";";
    }


    public String createAssetString(boolean prefix_file_type){
        String indentation = "      ";
        return  "\n"+indentation+"{\n" +
                indentation+"   \"name\" : \""+createName(prefix_file_type)+"\",\n"+
                indentation+"   \"id\" : "+id+",\n"+
                indentation+"   \"filePath\" : \""+path+"\",\n"+
                indentation+"   \"type\" : \""+type+"\"\n"+
                indentation+"},";
    }
}
