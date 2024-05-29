package com.jingdianjichi.interview.server.service;

import com.jingdianjichi.interview.api.req.InterviewReq;
import com.jingdianjichi.interview.api.vo.InterviewVO;

public interface InterviewService {

    InterviewVO analyse(InterviewReq req);

}
