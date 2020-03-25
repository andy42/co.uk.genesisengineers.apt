import resources.ResourceTree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main {
    public static void main (String[] args) {
        System.out.println("Working Directory = " +
                System.getProperty("user.dir"));

        File resourcesDir = getSubRootFolder(getRootDir(), "resources");


        ResourceTree resourceTree = new ResourceTree(resourcesDir);
        resourceTree.explorePath();
        resourceTree.log();

        createJavaRClass(getRootDir(),resourceTree.createResourceFile());
        createAssetFile(getRootDir(),resourceTree.createAssetFile());
    }

    private static File getRootDir(){
        return new File(System.getProperty("user.dir"));
    }

    private static File getSubRootFolder(File rootDir, String fileName){
        File resourcesDir = new File(rootDir.getAbsolutePath()+"/src/main/"+fileName);
        return resourcesDir;
    }

    private static File getGeneratedJavaDir(File rootDir,String fileName ){
        File resourcesDir = new File(rootDir.getAbsolutePath()+"/build/generated/java/"+fileName);
        return resourcesDir;
    }

    private static void writeDataTOFile(File file, String data){
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] strToBytes = data.getBytes();
            outputStream.write(strToBytes);
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createJavaRClass(File rootDir, String data){
        File rClassFile = getGeneratedJavaDir(rootDir, "R.java");
        writeDataTOFile(rClassFile, data);
    }

    private static void createAssetFile(File rootDir, String data){
        File rClassFile = getSubRootFolder(rootDir, "resources/assets.json");
        writeDataTOFile(rClassFile, data);
    }
}


//  src/main/resources/layouts