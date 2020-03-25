package resources;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DirectoryNode {
    private File file;
    private String type;
    private int depth;
    private String path;
    private List<DirectoryNode> directoryList = new ArrayList<>();
    private List<FileNode> fileList = new ArrayList<>();

    public DirectoryNode(File file, String parentPath, String type, int depth){
        this.file = file;
        this.type = type;
        this.depth = depth;

        if(parentPath.isEmpty()){
            this.path = file.getName();
        } else {
            this.path = parentPath+"/"+file.getName();
        }

    }

    private String createTabs(int count){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0; i<count; i++ ){
            stringBuilder.append("  ");
        }
        return stringBuilder.toString();
    }

    public int explorePath (int id){
        File[] childrenFileList = file.listFiles();

        for(File childFile : childrenFileList){
            if(childFile.isDirectory()){
                DirectoryNode newDirectoryNode = new DirectoryNode(childFile,path, type, depth+1);
                id = newDirectoryNode.explorePath(id);
                directoryList.add(newDirectoryNode);
            }
            if(childFile.isFile() && childFile.getName().isEmpty() == false){
                FileNode node = new FileNode(childFile, path, type, id++);
                if(node.getFileType().compareTo("DS_Store") == 0){
                    id--;
                    continue;
                }
                fileList.add(node);
            }
        }
        return id;
    }

    public String getType () {
        return type;
    }

    public void log(){
        for(FileNode fileNode : fileList){
            fileNode.log();
        }
        for(DirectoryNode directoryNode : directoryList){
            directoryNode.log();
        }
    }

    public void createResourceFile(StringBuilder stringBuilder, boolean isPrefixFileType){

        String indentation = createTabs(depth);
        stringBuilder.append(indentation+"public static final class "+file.getName()+" {\n");
        for(FileNode fileNode : fileList){
            stringBuilder.append(indentation+"  "+fileNode.createResourceString(isPrefixFileType)+"\n");
        }
        for(DirectoryNode directoryNode : directoryList){
            directoryNode.createResourceFile(stringBuilder, isPrefixFileType);
        }

        stringBuilder.append(indentation+"}\n");
    }

    public void createAssetFile(StringBuilder stringBuilder, boolean isPrefixFileType){

        for(FileNode fileNode : fileList){
            stringBuilder.append(fileNode.createAssetString(isPrefixFileType));
        }
        for(DirectoryNode directoryNode : directoryList){
            directoryNode.createAssetFile(stringBuilder, isPrefixFileType);
        }
    }

}
