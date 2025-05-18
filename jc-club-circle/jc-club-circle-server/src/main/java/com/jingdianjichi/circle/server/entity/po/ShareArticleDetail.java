package com.jingdianjichi.circle.server.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 文章详情
 *
 * DO 对应数据库实体类
 *
 * @author XuYifei
 * @date 2024-07-12
 */
@Data
@TableName("article_detail")
public class ShareArticleDetail {

    private static final long serialVersionUID = 1L;

    /**
     * 文章ID
     */
    private Long articleId;

    /**
     * 版本号
     */
    private Long version;

    /**
     * 文章内容
     */
    private String content;

    private Integer isDeleted;
}
