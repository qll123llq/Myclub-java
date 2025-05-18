package com.jingdianjichi.circle.api.enums;

import lombok.Getter;

/**
 * 删除状态枚举
 * 
 * @author: ChickenWing
 * @date: 2023/10/3
 */
@Getter
public enum CommemtReplyTypeEnum {

    COMMEMT(1,"评论"),
    COMMEMT_REPLY(2,"二级评论");

    public int code;

    public String desc;

    CommemtReplyTypeEnum(int code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public static CommemtReplyTypeEnum getByCode(int codeVal){
        for(CommemtReplyTypeEnum resultCodeEnum : CommemtReplyTypeEnum.values()){
            if(resultCodeEnum.code == codeVal){
                return resultCodeEnum;
            }
        }
        return null;
    }

}
