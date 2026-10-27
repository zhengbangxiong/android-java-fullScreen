package app.gkai.ai.modules.mine.beans.resp;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class DemandListResq implements Serializable {


    /**
     * content : [{"attachUrl":"","createTime":"","demandLogList":[{"createTime":"","demandId":0,"id":0,"logType":0,"memo":"","memoType":0,"platUserName":""}],"id":0,"isHasUnread":0,"isHasUnreadPlat":0,"memberId":0,"memberName":"","memo":"","status":0,"title":"","typeId":0,"typeName":""}]
     * empty : true
     * first : true
     * last : true
     * number : 0
     * numberOfElements : 0
     * pageable : {"page":0,"size":0,"sort":[]}
     * size : 0
     * sort : {"empty":true,"sorted":true,"unsorted":true}
     * totalElements : 0
     * totalPages : 0
     */

    private boolean empty;
    private boolean           first;
    private boolean           last;
    private int               number;
    private int               numberOfElements;
    private int               size;
    private SortBean          sort;
    private int               totalElements;
    private int               totalPages;
    private List<ContentBean> content;

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }


    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public SortBean getSort() {
        return sort;
    }

    public void setSort(SortBean sort) {
        this.sort = sort;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<ContentBean> getContent() {
        return content;
    }

    public void setContent(List<ContentBean> content) {
        this.content = content;
    }

    public static class SortBean {
        /**
         * empty : true
         * sorted : true
         * unsorted : true
         */

        private boolean empty;
        private boolean sorted;
        private boolean unsorted;

        public boolean isEmpty() {
            return empty;
        }

        public void setEmpty(boolean empty) {
            this.empty = empty;
        }

        public boolean isSorted() {
            return sorted;
        }

        public void setSorted(boolean sorted) {
            this.sorted = sorted;
        }

        public boolean isUnsorted() {
            return unsorted;
        }

        public void setUnsorted(boolean unsorted) {
            this.unsorted = unsorted;
        }
    }

    public static class ContentBean {
        /**
         * attachUrl :
         * createTime :
         * demandLogList : [{"createTime":"","demandId":0,"id":0,"logType":0,"memo":"","memoType":0,"platUserName":""}]
         * id : 0
         * isHasUnread : 0
         * isHasUnreadPlat : 0
         * memberId : 0
         * memberName :
         * memo :
         * status : 0
         * title :
         * typeId : 0
         * typeName :
         */

        private String attachUrl;
        private String                  createTime;
        private int                     id;
        private int                     isHasUnread;
        private int                     isHasUnreadPlat;
        private int                     memberId;
        private String                  memberName;
        private String                  memo;
        private int                     status;
        private String                  title;
        private int                     typeId;
        private String                  typeName;
        private List<DemandLogListBean> demandLogList;

        public String getAttachUrl() {
            return attachUrl;
        }

        public void setAttachUrl(String attachUrl) {
            this.attachUrl = attachUrl;
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

        public int getIsHasUnread() {
            return isHasUnread;
        }

        public void setIsHasUnread(int isHasUnread) {
            this.isHasUnread = isHasUnread;
        }

        public int getIsHasUnreadPlat() {
            return isHasUnreadPlat;
        }

        public void setIsHasUnreadPlat(int isHasUnreadPlat) {
            this.isHasUnreadPlat = isHasUnreadPlat;
        }

        public int getMemberId() {
            return memberId;
        }

        public void setMemberId(int memberId) {
            this.memberId = memberId;
        }

        public String getMemberName() {
            return memberName;
        }

        public void setMemberName(String memberName) {
            this.memberName = memberName;
        }

        public String getMemo() {
            return memo;
        }

        public void setMemo(String memo) {
            this.memo = memo;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getTypeId() {
            return typeId;
        }

        public void setTypeId(int typeId) {
            this.typeId = typeId;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public List<DemandLogListBean> getDemandLogList() {
            return demandLogList;
        }

        public void setDemandLogList(List<DemandLogListBean> demandLogList) {
            this.demandLogList = demandLogList;
        }

        public static class DemandLogListBean {
            /**
             * createTime :
             * demandId : 0
             * id : 0
             * logType : 0
             * memo :
             * memoType : 0
             * platUserName :
             */

            private String createTime;
            private int    demandId;
            private int    id;
            private int    logType;
            private String memo;
            private int    memoType;
            private String platUserName;

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public int getDemandId() {
                return demandId;
            }

            public void setDemandId(int demandId) {
                this.demandId = demandId;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getLogType() {
                return logType;
            }

            public void setLogType(int logType) {
                this.logType = logType;
            }

            public String getMemo() {
                return memo;
            }

            public void setMemo(String memo) {
                this.memo = memo;
            }

            public int getMemoType() {
                return memoType;
            }

            public void setMemoType(int memoType) {
                this.memoType = memoType;
            }

            public String getPlatUserName() {
                return platUserName;
            }

            public void setPlatUserName(String platUserName) {
                this.platUserName = platUserName;
            }
        }
    }
}
