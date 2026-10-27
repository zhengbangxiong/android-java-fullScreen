package app.gkai.ai.core.beans;

import java.util.List;

import lombok.Data;

@Data
public class Page<T> {

    /**
     * 总条数
     */
    private int totalElements;

    /**
     * 总页数
     */
    private int totalPages;

    /**
     * 数据列表
     */
    private List<T> content;

    /**
     * 数据
     */
    private int data;
}
