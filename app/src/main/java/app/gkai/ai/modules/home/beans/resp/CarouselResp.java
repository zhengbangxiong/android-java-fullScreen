package app.gkai.ai.modules.home.beans.resp;

import java.util.List;

import lombok.Data;

@Data
public class CarouselResp {


    private List<ContentBean> content;

    public List<ContentBean> getContent() {
        return content;
    }

    public void setContent(List<ContentBean> content) {
        this.content = content;
    }

    public static class ContentBean {
        /**
         * author :
         * clickNum : 0
         * columnId : 0
         * content :
         * createTime :
         * id : 0
         * imgFile :
         * imgUrl :
         * isBanner : 0
         * lang :
         * order : 0
         * publishTime :
         * source :
         * status : 0
         * summary :
         * title :
         */

        private String author;
        private int    clickNum;
        private int    columnId;
        private String content;
        private String createTime;
        private int    id;
        private String imgFile;
        private String imgUrl;
        private int    isBanner;
        private String lang;
        private int    order;
        private String publishTime;
        private String source;
        private int    status;
        private String summary;
        private String title;

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public int getClickNum() {
            return clickNum;
        }

        public void setClickNum(int clickNum) {
            this.clickNum = clickNum;
        }

        public int getColumnId() {
            return columnId;
        }

        public void setColumnId(int columnId) {
            this.columnId = columnId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImgFile() {
            return imgFile;
        }

        public void setImgFile(String imgFile) {
            this.imgFile = imgFile;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public int getIsBanner() {
            return isBanner;
        }

        public void setIsBanner(int isBanner) {
            this.isBanner = isBanner;
        }

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public String getPublishTime() {
            return publishTime;
        }

        public void setPublishTime(String publishTime) {
            this.publishTime = publishTime;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
