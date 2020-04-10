package resources;

import referenceFile.ReferenceFileFactory;
import util.FileLoader;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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

    public void setReferenceIds(ReferenceFileFactory referenceFileFactory){
        for(FileNode fileNode : fileList){
            int newId = referenceFileFactory.addId(path, fileNode.createRIdName());
            fileNode.setId(newId);
        }
        for(DirectoryNode directoryNode : directoryList){
            directoryNode.setReferenceIds(referenceFileFactory);
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

    public String getPath() {
        return path;
    }

    public String getType() {
        return type;
    }

    public List<FileNode> getFileList() {
        return fileList;
    }

    public void log(){
        for(FileNode fileNode : fileList){
            fileNode.log();
        }
        for(DirectoryNode directoryNode : directoryList){
            directoryNode.log();
        }
    }

    public void createDirAtRootDestination(Path destinationRoot){
        Path path = Paths.get(destinationRoot.toString(),getPath());
        FileLoader.createDir(path);
    }


    public void copyFiles(Path sourceRoot, Path destinationRoot){
        createDirAtRootDestination(destinationRoot);

        for(DirectoryNode directoryNode : directoryList){
            directoryNode.copyFiles(sourceRoot, destinationRoot);
        }
        for(FileNode fileNode : fileList){
            fileNode.copyFile(sourceRoot, destinationRoot);
        }
    }

    public void createAssetFile(StringBuilder stringBuilder, ReferenceFileFactory referenceFileFactory){

        for(FileNode fileNode : fileList){
            stringBuilder.append(fileNode.createAssetString(referenceFileFactory));
        }
        for(DirectoryNode directoryNode : directoryList){
            directoryNode.createAssetFile(stringBuilder, referenceFileFactory);
        }
    }

}
