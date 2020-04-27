package resources;

import assets.AssetsManager;
import referenceFile.ReferenceFileFactory;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ResourceTree {

    private File resourcesDir;

    private List<DirectoryNode> directoryList = new ArrayList<>();

    public ResourceTree(File resourcesDir){
        this.resourcesDir = resourcesDir;
    }

    public void explorePath (){
        File[] childrenFileList = resourcesDir.listFiles();
        int index = 0;
        for(File childFile : childrenFileList){
            if(childFile.isDirectory()){
                String type = childFile.getName();
                DirectoryNode newDirectoryNode = new DirectoryNode(childFile, "",type,1);
                index = newDirectoryNode.explorePath(index);
                directoryList.add(newDirectoryNode);
            }
        }
    }

    public void setReferenceIds(ReferenceFileFactory referenceFileFactory){
        for(DirectoryNode directoryNode : directoryList){
            switch (directoryNode.getType()){
                case "values":
                    continue;
            }
            directoryNode.setReferenceIds(referenceFileFactory);
        }
    }

    public void copyFiles(Path sourceRoot, Path destinationRoot, AssetsManager assetsManager, ReferenceFileFactory referenceFileFactory){
        for(DirectoryNode directoryNode : directoryList){
            switch (directoryNode.getType()){
                case "layouts":
                    //Skip layouts dir, will be handled by LayoutParser
                    break;
                case "values":
                    //Skip values dir, will be handled by ColorParser
                    break;
                default:
                    directoryNode.copyFiles(sourceRoot, destinationRoot, assetsManager, referenceFileFactory);
            }
        }
    }

    public void log(){
        for(DirectoryNode directoryNode : directoryList){
            directoryNode.log();
        }
    }

    public DirectoryNode getDirectoryNode(String directoryName){
        for(DirectoryNode directoryNode : directoryList){
            if(directoryNode.getType().compareTo(directoryName) == 0) {
                return directoryNode;
            }
        }
        return null;
    }

    public String createAssetFile(ReferenceFileFactory referenceFileFactory){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{\n");
        stringBuilder.append("  \"files\":[\n");

        for(DirectoryNode directoryNode : directoryList){
            directoryNode.createAssetFile(stringBuilder, referenceFileFactory);
        }
        stringBuilder.deleteCharAt(stringBuilder.length()-1);

        stringBuilder.append("\n  ]\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
