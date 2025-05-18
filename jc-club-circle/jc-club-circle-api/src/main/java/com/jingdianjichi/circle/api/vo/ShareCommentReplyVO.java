package com.jingdianjichi.circle.api.vo;

import com.jingdianjichi.circle.api.common.TreeNode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 评论及回复信息
 * </p>
 *
 * @author ChickenWing
 * @since 2024/05/16
 */
@Getter
@Setter
public class ShareCommentReplyVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 原始动态ID
     */
    private Long momentId;

    /**
     * 回复类型 1评论 2回复
     */
    private Integer replyType;

    /**
     * 子评论
     */

    private List<ShareCommentReplyVO> shareCommentReplyVOS;

    /**
     * 回复的评论
     */
    private String parentContent;
    /**
     * 内容
     */
    private String content;

    /**
     * 图片内容
     */
    private List<String> picUrlList;

    private String fromId;

    private Long toId;

    private String toName;

    private String toAvatar;

    private Long parentId;

    private String userName;

    private String avatar;

    /**
     * 创建人
     */
    private String createdBy;
    private long createdTime;


}
