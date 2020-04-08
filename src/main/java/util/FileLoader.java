package util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class FileLoader {

    public static String loadFileAsString (String filePath) {
        ClassLoader classLoader = FileLoader.class.getClassLoader();
        File file = new File(classLoader.getResource(filePath).getFile());
        return loadFileAsString(file);
    }

    public static String loadFileAsString (File file) {
        try {
            return FileUtils.readFileToString(file);
        } catch (Exception e) {
            Logger.error("loadFileAsString Exception " + e.getMessage());
            return null;
        }
    }

    public static List<File> listFiles(String filePath){
        ClassLoader classLoader = FileLoader.class.getClassLoader();
        File file = new File(classLoader.getResource(filePath).getFile());
        return Arrays.asList(file.listFiles());
    }

    public static void createDir(Path path){
        if(Files.exists(path)){
            return;
        }
        try {
            Files.createDirectory(path);
        } catch (IOException e) {
            Logger.error(e.getMessage(), e);
        }
    }

    public static void deleteDir(Path path){
        if(!Files.exists(path)){
            return;
        }
        try {
            Files.walk(path)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            Logger.error(e.getMessage(), e);
        }
    }

    public static boolean copyFile(Path source, Path dest ){
        try (InputStream fis = Files.newInputStream(source);
             OutputStream fos = Files.newOutputStream(dest)) {

            byte[] buffer = new byte[1024];
            int length;

            while ((length = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
        } catch (IOException e) {
            Logger.error(e.getMessage(), e);
            return false;
        }
        return true;
    }
}