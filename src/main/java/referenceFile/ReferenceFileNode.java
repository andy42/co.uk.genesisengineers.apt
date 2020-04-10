package referenceFile;

import java.util.HashMap;
import java.util.Map;

public class ReferenceFileNode {
    private String nodeName;
    private int depth;
    private Map<String, Integer> idMap= new HashMap();
    private Map<String, ReferenceFileNode> nodeMap= new HashMap();

    public ReferenceFileNode(String nodeName, int depth, int typeId){
        idMap.put("TYPE", typeId);
        this.nodeName = nodeName;
        this.depth = depth;
    }

    public boolean isSet(String name){
        return idMap.containsKey(name);
    }

    public void addId(String name, int index){
        idMap.put(name, index);
    }

    public Integer getId(String name){
        return idMap.get(name);
    }

    public ReferenceFileNode getNode(String name){
        return nodeMap.get(name);
    }

    public boolean hasNode(String name){
        return nodeMap.containsKey(name);
    }

    public ReferenceFileNode createNode(String name, int depth, int typeId){
        return nodeMap.put(name, new ReferenceFileNode(name, depth, typeId));
    }

    private String createTabs(int count){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0; i<count; i++ ){
            stringBuilder.append("  ");
        }
        return stringBuilder.toString();
    }

    public void createReferenceClass(StringBuilder stringBuilder){

        String indentation = createTabs(depth);
        stringBuilder.append(indentation+"public static final class "+nodeName+" {\n");

        for( String key : idMap.keySet()){
            stringBuilder.append(indentation+"  "+"public static final int "+key+" = "+idMap.get(key)+";\n");
        }

        for( String key : nodeMap.keySet()){
            nodeMap.get(key).createReferenceClass(stringBuilder);
        }

        stringBuilder.append(indentation+"}\n");
    }
}
