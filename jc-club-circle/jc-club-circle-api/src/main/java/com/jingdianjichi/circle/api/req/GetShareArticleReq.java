package com.jingdianjichi.circle.api.req;

import com.jingdianjichi.circle.api.common.PageInfo;
import lombok.Data;

import java.io.Serializable;

@Data
public class GetShareArticleReq implements Serializable {

    /**
     * 圈子ID
     */
    private Long circleId;

    /**
     * 分页信息
     */
    private PageInfo pageInfo;

    /**
     * 文章id
     */
    private Long id;
}