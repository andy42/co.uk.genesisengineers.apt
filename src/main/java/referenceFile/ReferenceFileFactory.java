package referenceFile;

import java.util.HashMap;
import java.util.Map;

public class ReferenceFileFactory {
    private Map<String, ReferenceFileNode> nodeMap= new HashMap();
    private int idIndex = 0;

    public boolean isSet(String type, String name){
        if(nodeMap.containsKey(type) == false) return false;
        return nodeMap.get(type).isSet(name);
    }

    public ReferenceFileNode getOrCreateNode(String path){
        String[] typeArray = path.split("/");
        if(typeArray.length == 0) return null;
        String nodeName = typeArray[0];

        ReferenceFileNode node = nodeMap.get(nodeName);
        if(node == null) {
            node = new ReferenceFileNode(nodeName, 1, idIndex++);
            nodeMap.put(nodeName, node);
        }

        for(int i=1; i< typeArray.length; i++){
            nodeName = typeArray[i];
            if(node.hasNode(nodeName)){
                node = node.getNode(nodeName);
            } else {
                node = node.createNode(nodeName, i+1, idIndex++);
            }
        }
        return node;
    }

    public ReferenceFileNode getNode(String path){
        String[] typeArray = path.split("/");
        if(typeArray.length == 0) return null;
        String nodeName = typeArray[0];

        ReferenceFileNode node = nodeMap.get(nodeName);
        if(node == null) return null;

        for(int i=1; i< typeArray.length; i++){
            nodeName = typeArray[i];
            node = node.getNode(nodeName);
        }
        return node;
    }

    public int addId(String path, String name){
        ReferenceFileNode node = getOrCreateNode(path);
        if(node.getId(name) != null){
            return node.getId(name);
        }
        node.addId(name,idIndex);
        return idIndex++;
    }

    public Integer getId(String path, String name){
        ReferenceFileNode node = getNode(path);
        return node.getId(name);
    }

    public String createJavaReferenceClass(String packageName){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("package "+packageName+";\n\n");
        stringBuilder.append("public final class R {\n");

        for(ReferenceFileNode referenceFileNode : nodeMap.values()){
            referenceFileNode.createJavaReferenceClass(stringBuilder);
        }
        stringBuilder.append("}\n");
        return stringBuilder.toString();
    }

    public String createKotlinReferenceClass(String packageName){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("package "+packageName+";\n\n");
        stringBuilder.append("class R {\n");

        for(ReferenceFileNode referenceFileNode : nodeMap.values()){
            referenceFileNode.createKotlinReferenceClass(stringBuilder);
        }
        stringBuilder.append("}\n");
        return stringBuilder.toString();
    }


    //this is takes full string ref "id/test"
    public Integer resIdLookUp(String ref) {
        String[] parts = ref.split("/");
        if(parts.length == 0) return null;
        String idName = parts[parts.length -1];

        String nodeName = parts[0];

        ReferenceFileNode node = nodeMap.get(nodeName);
        if(node == null) return null;

        for(int i=1; i< (parts.length -1); i++){
            nodeName = parts[i];
            node = node.getNode(nodeName);
            if(node == null) return null;
        }
        return node.getId(idName);
    }
}
