package com.jingdianjichi.subject.domain.handler.subject;

import com.jingdianjichi.subject.common.enums.SubjectInfoTypeEnum;
import com.jingdianjichi.subject.domain.entity.SubjectInfoBO;

/**
 * 判断题目的策略类
 * 
 * @author: ChickenWing
 * @date: 2023/10/5
 */
public class JudgeTypeHandler implements SubjectTypeHandler{
    
    @Override
    public SubjectInfoTypeEnum getHandlerType() {
        return SubjectInfoTypeEnum.JUDGE;
    }

    @Override
    public void add(SubjectInfoBO subjectInfoBO) {
    }
}
