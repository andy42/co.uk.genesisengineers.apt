package resources;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ResourceTree {

    private File resourcesDir;

    private List<DirectoryNode> directoryList = new ArrayList<>();
    private boolean isPrefixFileType = true;

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

    public void log(){
//        for(DirectoryNode directoryNode : directoryList){
//            directoryNode.log();
//        }
        //System.out.print(createResourceFile());
        System.out.print(createAssetFile());

    }

    public String createResourceFile(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("public final class R {\n");

        for(DirectoryNode directoryNode : directoryList){
            //stringBuilder.append(directoryNode.getType()+"\n");


            directoryNode.createResourceFile(stringBuilder,isPrefixFileType);
        }
        stringBuilder.append("}\n");
        return stringBuilder.toString();
    }
    public String createAssetFile(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{\n");
        stringBuilder.append("  \"files\":[\n");

        for(DirectoryNode directoryNode : directoryList){
            //stringBuilder.append(directoryNode.getType()+"\n");
            directoryNode.createAssetFile(stringBuilder, isPrefixFileType);
        }
        ;
        stringBuilder.deleteCharAt(stringBuilder.length()-1);

        stringBuilder.append("\n  ]\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
