package com.jingdianjichi.circle.server.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.base.Preconditions;
import com.jingdianjichi.circle.api.common.PageResult;
import com.jingdianjichi.circle.api.common.Result;
import com.jingdianjichi.circle.api.enums.IsDeletedFlagEnum;
import com.jingdianjichi.circle.api.req.GetShareArticleReq;
import com.jingdianjichi.circle.api.req.SaveShareArticleReplyReq;
import com.jingdianjichi.circle.api.req.SaveShareCommentReplyReq;
import com.jingdianjichi.circle.api.vo.ShareArticleVo;
import com.jingdianjichi.circle.api.vo.ShareMomentVO;
import com.jingdianjichi.circle.server.entity.po.ShareCircle;
import com.jingdianjichi.circle.server.entity.po.ShareCommentReply;
import com.jingdianjichi.circle.server.entity.po.ShareMoment;
import com.jingdianjichi.circle.server.sensitive.WordFilter;
import com.jingdianjichi.circle.server.service.ShareArticleService;
import com.jingdianjichi.circle.server.service.ShareCircleService;
import com.jingdianjichi.circle.server.util.LoginUtil;
import com.sun.org.apache.xalan.internal.xsltc.dom.AdaptiveResultTreeImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.UpdateProvider;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/circle/share/article")
public class ShareArticleController {

    @Resource
    private ShareCircleService shareCircleService;
    @Resource
    private WordFilter wordFilter;
    @Resource
    private ShareArticleService shareArticleService;

    @PostMapping(value = "/save")
    public Result save(@RequestBody SaveShareArticleReplyReq req) {
        try {
            if (log.isInfoEnabled()) {
                log.info("发布内容入参{}", JSON.toJSONString(req));
            }
            Preconditions.checkArgument(Objects.nonNull(req), "参数不能为空！");
            Preconditions.checkArgument(Objects.nonNull(req.getTitle()), "标题不能为空！");
            Preconditions.checkArgument(Objects.nonNull(req.getContent()), "内容不能为空！");
            Preconditions.checkArgument(Objects.nonNull(req.getCircleId()), "圈子不能为空！");


            ShareCircle data = shareCircleService.getById(req.getCircleId());
            Preconditions.checkArgument((Objects.nonNull(data) && data.getParentId() != -1), "非法圈子ID！");
            //敏感词过滤
            //wordFilter.check(req.getContent());
            req.setContent(wordFilter.replace(req.getContent()));
            shareArticleService.saveArticle(req);
            return Result.ok();
        } catch (IllegalArgumentException e) {
            log.error("参数异常！错误原因{}", e.getMessage(), e);
            return Result.fail(e.getMessage());
        } catch (Exception e) {
            log.error("发布内容异常！错误原因{}", e.getMessage(), e);
            return Result.fail("发布内容异常！");
        }
    }

    @PostMapping("/getArticles")
    @Transactional
    public Result<List<ShareArticleVo>> getList(@RequestBody GetShareArticleReq req) {
        try {
            if (log.isInfoEnabled()) {
                log.info("内容入参{}", JSON.toJSONString(req));
            }
            Preconditions.checkArgument(!Objects.isNull(req), "参数不能为空！");
            PageResult<ShareArticleVo> result = shareArticleService.getShareArticles(req);
            if (log.isInfoEnabled()) {
                log.info("内容出参{}", JSON.toJSONString(result));
            }
            return Result.ok(result);
        } catch (IllegalArgumentException e) {
            log.error("参数异常！错误原因{}", e.getMessage(), e);
            return Result.fail(e.getMessage());
        } catch (Exception e) {
            log.error("鸡圈内容异常！错误原因{}", e.getMessage(), e);
            return Result.fail("鸡圈内容异常！");
        }
    }

    @PostMapping("/getArticleDetail")
    @Transactional
    public Result<ShareArticleVo> getArticleDetail(@RequestBody GetShareArticleReq req) {
        try {
            if (log.isInfoEnabled()) {
                log.info("内容入参{}", JSON.toJSONString(req));
            }
            Preconditions.checkArgument(!Objects.isNull(req), "参数不能为空！");
            Preconditions.checkArgument(!Objects.isNull(req.getId()), "文章id不能为空！");
            ShareArticleVo result = shareArticleService.getShareArticleDetail(req);
            if (log.isInfoEnabled()) {
                log.info("内容出参{}", JSON.toJSONString(result));
            }
            return Result.ok(result);
        } catch (IllegalArgumentException e) {
            log.error("参数异常！错误原因{}", e.getMessage(), e);
            return Result.fail(e.getMessage());
        } catch (Exception e) {
            log.error("鸡圈内容异常！错误原因{}", e.getMessage(), e);
            return Result.fail("鸡圈内容异常！");
        }
    }
}
