package app.gkai.ai.modules.home.beans.req;

import lombok.Data;

@Data
public class FindAppUpdateRep {

    private String des;
    private String url;
    private String version;
    private int fileSize;
    private int isUpdate;

    public String getDes() {
        return des;
    }

    public String getUrl() {
        return url;
    }

    public String getVersion() {
        return version;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }
}
