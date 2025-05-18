package com.jingdianjichi.circle.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jingdianjichi.circle.api.common.PageInfo;
import com.jingdianjichi.circle.api.common.PageResult;
import com.jingdianjichi.circle.api.common.Result;
import com.jingdianjichi.circle.api.enums.IsDeletedFlagEnum;
import com.jingdianjichi.circle.api.req.GetShareArticleReq;
import com.jingdianjichi.circle.api.req.SaveShareArticleReplyReq;
import com.jingdianjichi.circle.api.vo.AuthorVo;
import com.jingdianjichi.circle.api.vo.ShareArticleVo;
import com.jingdianjichi.circle.server.config.context.LoginContextHolder;
import com.jingdianjichi.circle.server.dao.ShareArticleDetailMapper;
import com.jingdianjichi.circle.server.dao.ShareArticleMapper;
import com.jingdianjichi.circle.server.entity.dto.UserInfo;
import com.jingdianjichi.circle.server.entity.po.ShareArticle;
import com.jingdianjichi.circle.server.entity.po.ShareArticleDetail;
import com.jingdianjichi.circle.server.rpc.UserRpc;
import com.jingdianjichi.circle.server.service.ShareArticleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ShareArticleServiceImpl implements ShareArticleService {

    @Autowired
    private ShareArticleMapper shareArticleMapper;
    @Autowired
    private ShareArticleDetailMapper shareArticleDetailMapper;
    @Autowired
    private UserRpc userRpc;

    private ShareArticleServiceImpl() {
    }



    @Override
    public void saveArticle(SaveShareArticleReplyReq req) {
        ShareArticle shareArticle = new ShareArticle();
        shareArticle.setTitle(req.getTitle());
        shareArticle.setPicture(req.getAvatar());
        shareArticle.setSummary(req.getSummary());
        shareArticle.setIsDeleted(0);
        shareArticle.setCircleId(req.getCircleId());
        UserInfo userInfo = userRpc.getUserInfo(LoginContextHolder.getLoginId());
        shareArticle.setUserId(userInfo.getId());
        shareArticleMapper.insert(shareArticle);

        ShareArticleDetail shareArticleDetail = new ShareArticleDetail();
        shareArticleDetail.setArticleId(shareArticle.getId());
        shareArticleDetail.setIsDeleted(0);
        shareArticleDetail.setContent(req.getContent());
        shareArticleDetailMapper.insert(shareArticleDetail);
    }

    @Override
    public PageResult<ShareArticleVo> getShareArticles(GetShareArticleReq req) {

        LambdaQueryWrapper<ShareArticle> query = Wrappers.<ShareArticle>lambdaQuery()
                .eq(Objects.nonNull(req.getCircleId()), ShareArticle::getCircleId, req.getCircleId())
                .eq(ShareArticle::getIsDeleted, IsDeletedFlagEnum.UN_DELETED.getCode())
                .orderByDesc(ShareArticle::getUpdateTime);
        PageInfo pageInfo = req.getPageInfo();
        Page<ShareArticle> page = new Page<>(pageInfo.getPageNo(), pageInfo.getPageSize());

        Page<ShareArticle> pageRes = shareArticleMapper.selectPage(page, query);

        PageResult<ShareArticleVo> result = new PageResult<>();
        List<ShareArticle> records = pageRes.getRecords();

        List<Long> userIdList = records.stream().map(ShareArticle::getUserId).distinct().collect(Collectors.toList());
        Map<Long, UserInfo> userInfoMap = userRpc.batchGetUserInfoById(userIdList);
        UserInfo defaultUser = new UserInfo();

        List<ShareArticleVo> list = records.stream().map(item -> {
            ShareArticleVo vo = new ShareArticleVo();
            BeanUtils.copyProperties(item, vo);
            vo.setAvatar(item.getPicture());
//            vo.setContent(item.getContent());

//            vo.setReplyCount(item.getReplyCount());
//            vo.setCreatedTime(item.getCreatedTime().getTime());
            UserInfo user = userInfoMap.getOrDefault(item.getUserId(), defaultUser);
            vo.setUserName(user.getNickName());
            vo.setUserAvatar(user.getAvatar());
            return vo;
        }).collect(Collectors.toList());
        result.setRecords(list);
        result.setTotal((int) pageRes.getTotal());
        result.setPageSize(pageInfo.getPageSize());
        result.setPageNo(pageInfo.getPageNo());
        return result;
    }

    @Override
    public ShareArticleVo getShareArticleDetail(GetShareArticleReq req) {
        ShareArticle shareArticle = shareArticleMapper.selectById(req.getId());
        ShareArticleDetail shareArticleDetail = shareArticleDetailMapper.selectOne(new LambdaQueryWrapper<ShareArticleDetail>().eq(ShareArticleDetail::getArticleId, req.getId()));

        if (shareArticle == null || shareArticleDetail == null) {
            throw new RuntimeException("查不到该文章");
        }

        UserInfo userInfo = userRpc.getUserById(shareArticle.getUserId());

        AuthorVo authorVo = new AuthorVo();
        BeanUtils.copyProperties(userInfo, authorVo);
        ShareArticleVo shareArticleVo = new ShareArticleVo();
        BeanUtils.copyProperties(shareArticle, shareArticleVo);
        BeanUtils.copyProperties(shareArticleDetail, shareArticleVo);
        shareArticleVo.setAuthorVo(authorVo);
        return shareArticleVo;
    }
}
