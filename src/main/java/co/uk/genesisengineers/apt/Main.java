package co.uk.genesisengineers.apt;

import co.uk.genesisengineers.apt.assets.AssetsManager;
import co.uk.genesisengineers.apt.drawable.DrawableParser;
import co.uk.genesisengineers.apt.layout.LayoutParser;
import co.uk.genesisengineers.apt.referenceFile.ReferenceFileFactory;
import co.uk.genesisengineers.apt.resources.ResourceTree;
import co.uk.genesisengineers.apt.util.FileLoader;
import co.uk.genesisengineers.apt.util.Logger;
import co.uk.genesisengineers.apt.values.ValuesParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main (String[] args) {

        boolean useJava = false;

        System.out.println("Working Directory = " +
                System.getProperty("user.dir"));

        File resDir = getSubRootFolder(getRootDir(), "res");

        if(args.length == 0){
            throw new RuntimeException("args1 most be the java package Name e.g \"com.test.game\"");
        }
        String packageName = args[0];

        ResourceTree resourceTree = new ResourceTree(resDir);
        resourceTree.explorePath();

        AssetsManager assetsManager = new AssetsManager();

        ReferenceFileFactory referenceFileFactory = new ReferenceFileFactory();
        resourceTree.setReferenceIds(referenceFileFactory);

        Path resourcesPath = Paths.get(System.getProperty("user.dir"), "/src/main/resources");
        FileLoader.deleteDir(resourcesPath);
        FileLoader.createDir(resourcesPath);

        Path resPath = Paths.get(System.getProperty("user.dir"), "/src/main/res");
        resourceTree.copyFiles(resPath, resourcesPath, assetsManager, referenceFileFactory);

        ValuesParser valuesParser = new ValuesParser(resourceTree.getDirectoryNode("values"), referenceFileFactory,assetsManager);
        valuesParser.parse(resPath, resourcesPath);

        DrawableParser drawableParser = new DrawableParser(resourceTree.getDirectoryNode("drawables"), referenceFileFactory);
        drawableParser.parse(resPath, resourcesPath);

        LayoutParser layoutParser = new LayoutParser(resourceTree.getDirectoryNode("layouts"), referenceFileFactory);
        layoutParser.parse(resPath, resourcesPath, assetsManager, referenceFileFactory);

        if(useJava) {
            createJavaRClass(getRootDir(), referenceFileFactory.createJavaReferenceClass(packageName));
        } else {
            createKotlinRClass(getRootDir(), referenceFileFactory.createKotlinReferenceClass(packageName));
        }

        assetsManager.createAssetFile(resourcesPath);
    }

    private static File getRootDir(){
        return new File(System.getProperty("user.dir"));
    }

    private static File getSubRootFolder(File rootDir, String fileName){
        File resourcesDir = new File(rootDir.getAbsolutePath()+"/src/main/"+fileName);
        return resourcesDir;
    }

    private static void createDir(File rootDir, String path){
        File dir = new File(rootDir.getAbsolutePath()+path);
        if(dir.exists() == false){
            Logger.info("create : "+path);
            if(dir.mkdirs() == false){
                Logger.error("creating "+path);
            }
        }
    }

    private static File getGeneratedJavaDir(File rootDir,String fileName ){
        createDir(rootDir,"/build/generated/java" );
        File resourcesDir = new File(rootDir.getAbsolutePath()+"/build/generated/java/"+fileName);
        return resourcesDir;
    }

    private static File getGeneratedKotlinDir(File rootDir,String fileName ) {
        createDir(rootDir,"/build/generated/kotlin" );
        File resourcesDir = new File(rootDir.getAbsolutePath()+"/build/generated/kotlin/"+fileName);
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

    private static void createKotlinRClass(File rootDir, String data){
        File rClassFile = getGeneratedKotlinDir(rootDir, "R.kt");
        writeDataTOFile(rClassFile, data);
    }

    private static void createAssetFile(File rootDir, String data){
        File rClassFile = getSubRootFolder(rootDir, "resources/assets.json");
        writeDataTOFile(rClassFile, data);
    }
}