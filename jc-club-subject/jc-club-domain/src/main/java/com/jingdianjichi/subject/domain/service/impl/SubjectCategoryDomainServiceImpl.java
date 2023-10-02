package com.jingdianjichi.subject.domain.service.impl;

import com.alibaba.fastjson.JSON;
import com.jingdianjichi.subject.domain.convert.SubjectCategoryConverter;
import com.jingdianjichi.subject.domain.entity.SubjectCategoryBO;
import com.jingdianjichi.subject.domain.service.SubjectCategoryDomainService;
import com.jingdianjichi.subject.infra.basic.entity.SubjectCategory;
import com.jingdianjichi.subject.infra.basic.service.SubjectCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class SubjectCategoryDomainServiceImpl implements SubjectCategoryDomainService {

    @Resource
    private SubjectCategoryService subjectCategoryService;

    public void add(SubjectCategoryBO subjectCategoryBO) {
        if(log.isInfoEnabled()){
            log.info("SubjectCategoryController.add.bo:{}", JSON.toJSONString(subjectCategoryBO));
        }
        SubjectCategory subjectCategory = SubjectCategoryConverter.INSTANCE
                .convertBoToCategory(subjectCategoryBO);
        subjectCategoryService.insert(subjectCategory);
    }

    public List<SubjectCategoryBO> queryPrimaryCategory() {
        SubjectCategory subjectCategory = new SubjectCategory();
        subjectCategory.setParentId(0L);
        List<SubjectCategory> subjectCategoryList = subjectCategoryService.queryPrimaryCategory(subjectCategory);
        List<SubjectCategoryBO> boList = SubjectCategoryConverter.INSTANCE
                .convertBoToCategory(subjectCategoryList);
        if(log.isInfoEnabled()){
            log.info("SubjectCategoryController.queryPrimaryCategory.boList:{}",
                    JSON.toJSONString(boList));
        }
        return boList;
    }

}
