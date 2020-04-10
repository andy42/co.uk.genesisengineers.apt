package layout;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import referenceFile.ReferenceFileFactory;
import resources.DirectoryNode;
import resources.FileNode;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LayoutParser {
    private ReferenceFileFactory referenceFileFactory;
    private DirectoryNode rootDirectoryNode;

    public LayoutParser(DirectoryNode directoryNode, ReferenceFileFactory referenceFileFactory){
        this.rootDirectoryNode = directoryNode;
        this.referenceFileFactory = referenceFileFactory;
    }

    public boolean parse(Path sourceRoot, Path destinationRoot ){

        rootDirectoryNode.createDirAtRootDestination(destinationRoot);

        for(FileNode fileNode : rootDirectoryNode.getFileList()){
            switch (fileNode.getFileType()){
                case "xml":
                    parseFile(
                            fileNode.getPath(sourceRoot),
                            Paths.get(destinationRoot.toString(), fileNode.getPathString()));
                    break;
                case "xsd":
                    fileNode.copyFile(sourceRoot, destinationRoot);
                    break;
            }
        }
        return true;
    }

    private void parseFile(Path filePath, Path destinationPath ) {
        try {

            SAXBuilder builder = new SAXBuilder();
            Document doc = (Document) builder.build(Files.newInputStream(filePath));
            parseView(doc.getRootElement());

            XMLOutputter xmlOutput = new XMLOutputter();
            xmlOutput.setFormat(Format.getPrettyFormat());
            xmlOutput.output(doc, Files.newOutputStream(destinationPath));

        } catch (IOException io) {
            io.printStackTrace();
        } catch (JDOMException e) {
            e.printStackTrace();
        }
    }

    private void parseView(Element element){
        final String name = element.getName();
        final String id = element.getAttributeValue( "id");

        for(Attribute attribute : element.getAttributes()){
            attribute.getValue();
        }

        if(id != null){
            element.setAttribute("id", Integer.toString(referenceFileFactory.addId("id",id )) );
        }

        for(Element ChildElement : element.getChildren()){
            parseView(ChildElement);
        }
    }
}
