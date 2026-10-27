package app.gkai.ai.modules.home.beans.resp;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class ArticleResp implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 文章ID
     */
    private Integer id;


    /**
     * 所属栏目ID
     */
    private Integer columnId;

    /**
     * 文章标题
     */
    private String title;


    /**
     * 文章摘要
     */
    private String summary;


    /**
     * 文章缩略图
     */
    private String imgFile;


    /**
     * 文章来源
     */
    private String source;


    /**
     * 文章作者
     */
    private String author;


    /**
     * 发布时间
     */
    private Date publishTime;


    /**
     * 点击次数
     */
    private Integer clickNum;


    /**
     * 是否横幅图，0表示否，1表示是
     */
    private Integer isBanner;


    /**
     * 文章内容
     */
    private String content;


    /**
     * 文章排序，按升序排
     */
    private Integer order;


    /**
     * 发布时间
     */
    private Date createTime;


    /**
     * 点击跳转链接
     */
    private String imgUrl;


    /**
     * lang 语言
     */
    private String lang;


}
