package com.jingdianjichi.circle.api.req;

import lombok.Data;

@Data
public class SaveShareArticleReplyReq {
    private String title;
    private String content;
    private String avatar;
    private String summary;
    private Long circleId;
}
