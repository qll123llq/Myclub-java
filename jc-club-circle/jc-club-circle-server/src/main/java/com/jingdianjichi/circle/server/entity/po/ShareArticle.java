package com.jingdianjichi.circle.server.entity.po;




import java.io.Serializable;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
* 文章表
* @TableName article
*/
@Data
@TableName("Article")
public class ShareArticle implements Serializable {

    /**
    * 主键ID
    */
    private Long id;
    /**
    * 用户id
    */
    private Long userId;
    /**
    * 文章类型：1-博文，2-问答
    */
    private Integer articleType;
    /**
    * 文章标题
    */
    private String title;
    /**
    * 短标题
    */
    private String shortTitle;
    /**
    * 文章头图
    */
    private String picture;
    /**
    * 文章摘要
    */
    private String summary;
    /**
    * 类目ID
    */
    private Integer categoryId;
    /**
    * 来源：1-转载，2-原创，3-翻译
    */
    private Integer source;
    /**
    * 原文链接
    */
    private String sourceUrl;
    /**
    * 官方状态：0-非官方，1-官方
    */
    private Integer officalStat;
    /**
    * 置顶状态：0-不置顶，1-置顶
    */
    private Integer toppingStat;
    /**
    * 加精状态：0-不加精，1-加精
    */
    private Integer creamStat;
    /**
    * 状态：0-未发布，1-已发布
    */
    private Integer status;
    /**
    * 是否删除
    */
    private Integer isDeleted;
    /**
    * 创建时间
    */
    private Date createTime;
    /**
    * 最后更新时间
    */
    private Date updateTime;

    /**
     * 圈子id
     */
    private Long circleId;
}
