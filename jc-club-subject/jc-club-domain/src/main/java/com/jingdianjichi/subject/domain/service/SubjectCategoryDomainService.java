package com.jingdianjichi.subject.domain.service;

import com.jingdianjichi.subject.domain.entity.SubjectCategoryBO;

import java.util.List;


public interface SubjectCategoryDomainService {

    void add(SubjectCategoryBO subjectCategoryBO);

    /**
     * 查询岗位大类
     */
    List<SubjectCategoryBO> queryPrimaryCategory();


}
