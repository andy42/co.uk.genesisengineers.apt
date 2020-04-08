import layout.LayoutParser;
import referenceFile.ReferenceFileFactory;
import resources.ResourceTree;
import util.FileLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main (String[] args) {
        System.out.println("Working Directory = " +
                System.getProperty("user.dir"));

        File resDir = getSubRootFolder(getRootDir(), "res");

        if(args.length == 0){
            throw new RuntimeException("args1 most be the java package Name e.g \"com.test.game\"");
        }
        String packageName = args[0];

        ResourceTree resourceTree = new ResourceTree(resDir);
        resourceTree.explorePath();

        ReferenceFileFactory referenceFileFactory = new ReferenceFileFactory();
        resourceTree.setReferenceIds(referenceFileFactory);

        Path resourcesPath = Paths.get(System.getProperty("user.dir"), "/src/main/resources");
        FileLoader.deleteDir(resourcesPath);
        FileLoader.createDir(resourcesPath);

        Path resPath = Paths.get(System.getProperty("user.dir"), "/src/main/res");
        resourceTree.copyFiles(resPath, resourcesPath);


        LayoutParser layoutParser = new LayoutParser(resourceTree.getDirectoryNode("layouts"), referenceFileFactory);
        layoutParser.parse(resPath, resourcesPath);

        createJavaRClass(getRootDir(), referenceFileFactory.createReferenceClass(packageName));
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