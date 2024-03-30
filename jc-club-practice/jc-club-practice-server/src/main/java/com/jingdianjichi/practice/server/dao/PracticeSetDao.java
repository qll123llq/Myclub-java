package com.jingdianjichi.practice.server.dao;

import com.jingdianjichi.practice.server.entity.dto.PracticeSetDTO;
import com.jingdianjichi.practice.server.entity.po.PracticeSetPO;

import java.util.List;

public interface PracticeSetDao {

    /**
     * 新增套题
     */
    int add(PracticeSetPO po);

    PracticeSetPO selectById(Long setId);

    int updateHeat(Long setId);
}
