package co.uk.genesisengineers.apt.assets;

public class Asset {
    private String name;
    private String id;
    private String filePath;
    private String fileType;
    private String type;
    private String assetId = "";

    private static final String indentation = "      ";

    public String createAssetString(){
        return  "\n"+indentation+"{\n" +
                indentation+"   \"name\" : \""+name+"\",\n"+
                indentation+"   \"id\" : "+id+",\n"+
                indentation+"   \"assetId\" : \""+assetId+"\",\n"+
                indentation+"   \"filePath\" : \""+filePath+"\",\n"+
                indentation+"   \"fileType\" : \""+fileType+"\",\n"+
                indentation+"   \"type\" : \""+type+"\"\n"+
                indentation+"},";
    }

    public static class AssetBuilder{
        private String name;
        private String id;
        private String filePath;
        private String fileType;
        private String type;

        public AssetBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public AssetBuilder setId(String id) {
            this.id = id;
            return this;
        }

        public AssetBuilder setFilePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public AssetBuilder setFileType(String fileType) {
            this.fileType = fileType;
            return this;
        }

        public AssetBuilder setType(String type) {
            this.type = type;
            return this;
        }

        public Asset build(){
            Asset asset = new Asset();
            asset.name = this.name;
            asset.id = this.id;
            asset.filePath = this.filePath;
            asset.fileType = this.fileType;
            asset.name = this.name;
            asset.type = this.type;


            String[] pathArray = filePath.split("/");

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("@");
            
            if(pathArray.length > 1){
                for(int i= 0; i< (pathArray.length -1); i++){
                    stringBuilder.append(pathArray[i]+"/");
                }
            }
            else if (pathArray.length == 1){
                stringBuilder.append(pathArray[0]+"/");
            }
            stringBuilder.append(name);
            asset.assetId = stringBuilder.toString();

            return asset;
        }
    }
}
