import resources.ResourceTree;

import java.io.File;

public class Main {
    public static void main (String[] args) {
        System.out.println("Working Directory = " +
                System.getProperty("user.dir"));

        File resourcesDir = getResourcesDir(getRootDir());

        ResourceTree resourceTree = new ResourceTree(resourcesDir);
        resourceTree.explorePath();
        resourceTree.log();
    }

    private static File getRootDir(){
        return new File(System.getProperty("user.dir"));
    }

    private static File getResourcesDir(File rootDir){
        File resourcesDir = new File(rootDir.getAbsolutePath()+"/src/main/resources");
        return resourcesDir;
    }
}


//  src/main/resources/layouts