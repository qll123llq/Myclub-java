package com.jingdianjichi.circle.server.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jingdianjichi.circle.api.enums.IsDeletedFlagEnum;
import com.jingdianjichi.circle.api.enums.CommemtReplyTypeEnum;
import com.jingdianjichi.circle.api.req.GetShareCommentReq;
import com.jingdianjichi.circle.api.req.RemoveShareCommentReq;
import com.jingdianjichi.circle.api.req.SaveShareCommentReplyReq;
import com.jingdianjichi.circle.api.vo.ShareCommentReplyVO;
import com.jingdianjichi.circle.server.dao.ShareArticleMapper;
import com.jingdianjichi.circle.server.dao.ShareCommentReplyMapper;
import com.jingdianjichi.circle.server.dao.ShareMomentMapper;
import com.jingdianjichi.circle.server.entity.dto.UserInfo;
import com.jingdianjichi.circle.server.entity.po.ShareArticle;
import com.jingdianjichi.circle.server.entity.po.ShareCommentReply;
import com.jingdianjichi.circle.server.entity.po.ShareMoment;
import com.jingdianjichi.circle.server.rpc.UserRpc;
import com.jingdianjichi.circle.server.service.ShareArticleService;
import com.jingdianjichi.circle.server.service.ShareCommentReplyService;
import com.jingdianjichi.circle.server.util.LoginUtil;
import com.jingdianjichi.circle.server.util.TreeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 评论及回复信息 服务实现类
 * </p>
 *
 * @author ChickenWing
 * @since 2024/05/16
 */
@Service
public class ShareCommentReplyServiceImpl extends ServiceImpl<ShareCommentReplyMapper, ShareCommentReply> implements ShareCommentReplyService {

    @Resource
    private ShareMomentMapper shareMomentMapper;

    @Resource
    private ShareArticleMapper shareArticleMapper;

    @Resource
    private ShareCommentReplyMapper shareCommentReplyMapper;

    @Resource
    private UserRpc userRpc;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveComment(SaveShareCommentReplyReq req) {

        ShareArticle shareArticle = shareArticleMapper.selectById(req.getMomentId());
//
//        ShareMoment moment = shareMomentMapper.selectById(req.getMomentId());
        ShareCommentReply comment = new ShareCommentReply();
        comment.setMomentId(req.getMomentId());
        comment.setReplyType(req.getReplyType());
        String loginId = LoginUtil.getLoginId();
        // 1评论 2回复
        if (req.getReplyType() == CommemtReplyTypeEnum.COMMEMT_REPLY.getCode()) {
            comment.setToId(req.getTargetId());
            comment.setReplyId(req.getReplyId());
        }
        comment.setContent(req.getContent());
        if (!CollectionUtils.isEmpty(req.getPicUrlList())) {
            comment.setPicUrls(JSON.toJSONString(req.getPicUrlList()));
        }
        comment.setCreatedBy(LoginUtil.getLoginId());
        comment.setCreatedTime(new Date());
        comment.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.getCode());
        comment.setToUser(loginId);
//        shareMomentMapper.incrReplyCount(moment.getId(), 1);
        return super.save(comment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeComment(RemoveShareCommentReq req) {
        ShareCommentReply comment = getById(req.getId());
        LambdaQueryWrapper<ShareCommentReply> query = Wrappers.<ShareCommentReply>lambdaQuery()
                .eq(ShareCommentReply::getMomentId, comment.getMomentId())
                .eq(ShareCommentReply::getIsDeleted, IsDeletedFlagEnum.UN_DELETED.getCode()).select(ShareCommentReply::getId,
                        ShareCommentReply::getMomentId,
                        ShareCommentReply::getReplyType,
                        ShareCommentReply::getContent,
                        ShareCommentReply::getPicUrls,
                        ShareCommentReply::getCreatedBy,
                        ShareCommentReply::getToUser,
                        ShareCommentReply::getParentId);
        List<ShareCommentReply> list = list(query);
        List<ShareCommentReply> replyList = new ArrayList<>();
//        List<ShareCommentReply> tree = TreeUtils.buildTree(list);
//        for (ShareCommentReply reply : tree) {
//            TreeUtils.findAll(replyList, reply, req.getId());
//        }
        // 关联子级对象及 moment 的回复数量
        Set<Long> ids = replyList.stream().map(ShareCommentReply::getId).collect(Collectors.toSet());
        LambdaUpdateWrapper<ShareCommentReply> update = Wrappers.<ShareCommentReply>lambdaUpdate()
                .eq(ShareCommentReply::getMomentId, comment.getMomentId())
                .in(ShareCommentReply::getId, ids);
        ShareCommentReply updateEntity = new ShareCommentReply();
        updateEntity.setIsDeleted(IsDeletedFlagEnum.DELETED.getCode());
        int count = getBaseMapper().update(updateEntity, update);
        shareMomentMapper.incrReplyCount(comment.getMomentId(), -count);
        return true;
    }

    @Override
    public List<ShareCommentReplyVO> listComment(GetShareCommentReq req) {
        LambdaQueryWrapper<ShareCommentReply> query = Wrappers.<ShareCommentReply>lambdaQuery()
                .eq(ShareCommentReply::getMomentId, req.getId())
//                .eq(ShareCommentReply::getReplyType, CommemtReplyTypeEnum.COMMEMT.getCode())
                .eq(ShareCommentReply::getIsDeleted, IsDeletedFlagEnum.UN_DELETED.getCode())
                .select(ShareCommentReply::getId,
                        ShareCommentReply::getMomentId,
                        ShareCommentReply::getReplyType,
                        ShareCommentReply::getContent,
                        ShareCommentReply::getPicUrls,
                        ShareCommentReply::getCreatedBy,
                        ShareCommentReply::getToUser,
                        ShareCommentReply::getToId,
                        ShareCommentReply::getReplyId,
                        ShareCommentReply::getCreatedTime,
                        ShareCommentReply::getCreatedBy,
                        ShareCommentReply::getParentId);
        List<ShareCommentReply> list = list(query);
        List<String> userNameList = list.stream().map(ShareCommentReply::getCreatedBy).distinct().collect(Collectors.toList());
        List<String> toUserNameList = list.stream().map(ShareCommentReply::getToUser).distinct().collect(Collectors.toList());
        userNameList.addAll(toUserNameList);
        Map<String, UserInfo> userInfoMap = userRpc.batchGetUserInfo(userNameList);




        //所有的一级评论
        List<ShareCommentReply> list1 = list.stream().filter(item -> item.getReplyType() == CommemtReplyTypeEnum.COMMEMT.getCode()).collect(Collectors.toList());

        Collections.sort(list1, (a,b) -> {
            long ans = b.getCreatedTime().getTime() - a.getCreatedTime().getTime();
            if (ans >= 0) {
                return 1;
            } else if (ans < 0) {
                return -1;
            }
            return 0;
        });
        //所有的二级评论
        List<ShareCommentReply> list2 = list.stream().filter(item -> item.getReplyType() == CommemtReplyTypeEnum.COMMEMT_REPLY.getCode()).collect(Collectors.toList());
        List<Long> replyIds = list2.stream().map(item ->
                item.getReplyId()
        ).collect(Collectors.toList());

        Map<Long, String> commentParents = null;
        if (!CollectionUtils.isEmpty(replyIds)) {
            commentParents = shareCommentReplyMapper.selectList(new LambdaQueryWrapper<ShareCommentReply>()
                            .in(ShareCommentReply::getId, replyIds)).stream()
                    .collect(Collectors.toMap(ShareCommentReply::getId, e -> e.getContent()));
        }


        UserInfo defaultUser = new UserInfo();
        List<ShareCommentReplyVO> voList = new ArrayList<>();
        for (int i = 0; i < list1.size(); i++) {
            ShareCommentReply item = list1.get(i);
            ShareCommentReplyVO vo = new ShareCommentReplyVO();
            vo.setId(item.getId());
            vo.setMomentId(item.getMomentId());
            vo.setReplyType(item.getReplyType());
            vo.setContent(item.getContent());
            vo.setCreatedBy(item.getCreatedBy());
            if (Objects.nonNull(item.getPicUrls())) {
                vo.setPicUrlList(JSONArray.parseArray(item.getPicUrls(), String.class));
            }
            vo.setParentId(item.getParentId());
            UserInfo user = userInfoMap.getOrDefault(item.getCreatedBy(), defaultUser);
            vo.setUserName(user.getNickName());
            vo.setAvatar(user.getAvatar());
            vo.setCreatedTime(item.getCreatedTime().getTime());
            if (StringUtils.isNotBlank(item.getToUser())) {
                UserInfo toUser = userInfoMap.getOrDefault(item.getToUser(), defaultUser);
                vo.setToAvatar(toUser.getAvatar());
                vo.setToName(toUser.getNickName());
            }

            List<ShareCommentReply> shareCommentReplies = getCommentReplyByParentId(item.getId(), list2);
            vo.setShareCommentReplyVOS(buildShareCommentReplyVOList(shareCommentReplies, userInfoMap, commentParents));
            voList.add(vo);
        }
//        List<ShareCommentReplyVO> voList = list1.stream().map(item -> {
//            ShareCommentReplyVO vo = new ShareCommentReplyVO();
//            vo.setId(item.getId());
//            vo.setMomentId(item.getMomentId());
//            vo.setReplyType(item.getReplyType());
//            vo.setContent(item.getContent());
//            vo.setCreatedBy(item.getCreatedBy());
//            if (Objects.nonNull(item.getPicUrls())) {
//                vo.setPicUrlList(JSONArray.parseArray(item.getPicUrls(), String.class));
//            }
//            vo.setParentId(item.getParentId());
//            UserInfo user = userInfoMap.getOrDefault(item.getCreatedBy(), defaultUser);
//            vo.setUserName(user.getNickName());
//            vo.setAvatar(user.getAvatar());
//            vo.setCreatedTime(item.getCreatedTime().getTime());
//            if (StringUtils.isNotBlank(item.getToUser())) {
//                UserInfo toUser = userInfoMap.getOrDefault(item.getToUser(), defaultUser);
//                vo.setToAvatar(toUser.getAvatar());
//                vo.setToName(toUser.getNickName());
//            }
//
//            List<ShareCommentReply> shareCommentReplies = shareCommentReplyMapper.selectList(new LambdaQueryWrapper<ShareCommentReply>().eq(ShareCommentReply::getToId, item.getId()));
//
//            vo.setShareCommentReplyVOS(buildShareCommentReplyVOList(shareCommentReplies, userInfoMap, commentParents));
//            return vo;
//        }).collect(Collectors.toList());
        return voList;
    }

    private List<ShareCommentReply> getCommentReplyByParentId(Long id, List<ShareCommentReply> replys) {


        if (replys != null && replys.size() > 0) {
            return replys.stream().filter(item -> item.getToId() == id).collect(Collectors.toList());
        }
        return null;
    }
    private List<ShareCommentReplyVO> buildShareCommentReplyVOList(List<ShareCommentReply> list, Map<String, UserInfo> userInfoMap, Map<Long, String> commentParents) {
        if (list == null) {
            return null;
        }
        UserInfo defaultUser = new UserInfo();
        List<ShareCommentReplyVO> voList = list.stream().map(item -> {
            ShareCommentReplyVO vo = new ShareCommentReplyVO();
            vo.setId(item.getId());
            vo.setMomentId(item.getMomentId());
            vo.setReplyType(item.getReplyType());
            vo.setContent(item.getContent());
            vo.setCreatedBy(item.getCreatedBy());
            if (Objects.nonNull(item.getPicUrls())) {
                vo.setPicUrlList(JSONArray.parseArray(item.getPicUrls(), String.class));
            }
            vo.setParentId(item.getParentId());
            UserInfo user = userInfoMap.getOrDefault(item.getCreatedBy(), defaultUser);
            vo.setUserName(user.getNickName());
            vo.setAvatar(user.getAvatar());
            vo.setCreatedTime(item.getCreatedTime().getTime());

            if (!CollectionUtils.isEmpty(commentParents) &&item.getReplyId() != null) {
                vo.setParentContent(commentParents.getOrDefault(item.getReplyId(), null));
            }

            if (userInfoMap!= null && StringUtils.isNotBlank(item.getToUser())) {
                UserInfo toUser = userInfoMap.getOrDefault(item.getToUser(), defaultUser);
                vo.setToAvatar(toUser.getAvatar());
                vo.setToName(toUser.getNickName());
            }

            return vo;
        }).collect(Collectors.toList());
        return voList;
    }
}
