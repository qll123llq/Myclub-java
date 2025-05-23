package com.jingdianjichi.circle.server.service;



import com.jingdianjichi.circle.api.common.PageResult;
import com.jingdianjichi.circle.api.common.Result;
import com.jingdianjichi.circle.api.req.GetShareArticleReq;
import com.jingdianjichi.circle.api.req.SaveShareArticleReplyReq;
import com.jingdianjichi.circle.api.vo.ShareArticleVo;
import com.jingdianjichi.circle.server.entity.po.ShareArticle;
import com.jingdianjichi.circle.server.entity.po.ShareMoment;


public interface ShareArticleService {
    void saveArticle(SaveShareArticleReplyReq req);
    PageResult<ShareArticleVo> getShareArticles(GetShareArticleReq req);

    ShareArticleVo getShareArticleDetail(GetShareArticleReq req);

    ShareArticle getById(Long momentId);
}
