package com.jingdianjichi.practice.server.service;

import com.jingdianjichi.practice.api.req.SubmitPracticeDetailReq;

public interface PracticeDetailService {

    /**
     * 提交练题情况
     */
    Boolean submit(SubmitPracticeDetailReq req);


}