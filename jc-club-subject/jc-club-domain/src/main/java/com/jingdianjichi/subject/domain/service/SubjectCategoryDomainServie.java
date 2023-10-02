package com.jingdianjichi.subject.domain.service;

import com.jingdianjichi.subject.domain.entity.SubjectCategoryBO;
import com.jingdianjichi.subject.infra.basic.entity.SubjectCategory;
import org.springframework.stereotype.Service;


public interface SubjectCategoryDomainServie {

    void add(SubjectCategoryBO subjectCategoryBO);

}
