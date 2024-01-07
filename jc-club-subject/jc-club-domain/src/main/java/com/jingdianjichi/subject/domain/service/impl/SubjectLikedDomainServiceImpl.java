package com.jingdianjichi.subject.domain.service.impl;

import com.jingdianjichi.subject.common.enums.IsDeletedFlagEnum;
import com.jingdianjichi.subject.domain.convert.SubjectLikedBOConverter;
import com.jingdianjichi.subject.domain.entity.SubjectLikedBO;
import com.jingdianjichi.subject.domain.service.SubjectLikedDomainService;
import com.jingdianjichi.subject.infra.basic.entity.SubjectLiked;
import com.jingdianjichi.subject.infra.basic.service.SubjectLikedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 题目点赞表 领域service实现了
 *
 * @author jingdianjichi
 * @since 2024-01-07 23:08:45
 */
@Service
@Slf4j
public class SubjectLikedDomainServiceImpl implements SubjectLikedDomainService {

    @Resource
    private SubjectLikedService subjectLikedService;

    @Override
    public Boolean add(SubjectLikedBO subjectLikedBO) {
        SubjectLiked subjectLiked = SubjectLikedBOConverter.INSTANCE.convertBOToEntity(subjectLikedBO);
        subjectLiked.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.getCode());
        return subjectLikedService.insert(subjectLiked) > 0;
    }

    @Override
    public Boolean update(SubjectLikedBO subjectLikedBO) {
        SubjectLiked subjectLiked = SubjectLikedBOConverter.INSTANCE.convertBOToEntity(subjectLikedBO);
        return subjectLikedService.update(subjectLiked) > 0;
    }

    @Override
    public Boolean delete(SubjectLikedBO subjectLikedBO) {
        SubjectLiked subjectLiked = new SubjectLiked();
        subjectLiked.setId(subjectLikedBO.getId());
        subjectLiked.setIsDeleted(IsDeletedFlagEnum.DELETED.getCode());
        return subjectLikedService.update(subjectLiked) > 0;
    }

}
